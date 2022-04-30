package com.company.enroller.controllers;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.times;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.servlet.MockMvc;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@RunWith(SpringRunner.class)
@WebMvcTest(ParticipantRestController.class)
public class ParticipantRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MeetingService meetingService;

	@MockBean
	private ParticipantService participantService;

	@Test
	public void getParticipants() throws Exception {
		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");

		Collection<Participant> allParticipants = singletonList(participant);
		given(participantService.getAll()).willReturn(allParticipants);

		mvc.perform(get("/participants").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].login", is(participant.getLogin())));
	}

	@Test
	public void addParticipant() throws Exception {
		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");
		String inputJSON = "{\"login\":\"testlogin\", \"password\":\"somepassword\"}";

		given(participantService.findByLogin("testlogin")).willReturn((Participant)null);
		mvc.perform(post("/participants").content(inputJSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		given(participantService.findByLogin("testlogin")).willReturn(participant);
		mvc.perform(post("/participants").content(inputJSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());

		verify(participantService, Mockito.times(2)).findByLogin("testlogin");
	}

	@Test
	public void deleteParticipant() throws Exception {
		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");
		String inputJSON = "{\"login\":\"testlogin\", \"password\":\"somepassword\"}";

		given(participantService.findByLogin("testlogin")).willReturn(participant);
		mvc.perform(delete("/participants/testlogin").content(inputJSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		given(participantService.findByLogin("testlogin")).willReturn((Participant)null);
		mvc.perform(delete("/participants/testlogin").content(inputJSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

		verify(participantService, Mockito.times(2)).findByLogin("testlogin");
	}


	@Test
	public void updateParticipant() throws Exception {
		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");
		String inputJSON = "{\"login\":\"testlogin\", \"password\":\"somepasswordupdate\"}";

		given(participantService.findByLogin("testlogin")).willReturn((Participant)null);
		mvc.perform(put("/participants/testlogin").content(inputJSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

		given(participantService.findByLogin("testlogin")).willReturn(participant);
		mvc.perform(put("/participants/testlogin").content(inputJSON).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
		verify(participantService, Mockito.times(2)).findByLogin("testlogin");
	}

}
