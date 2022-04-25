package com.company.enroller.persistence;

import java.util.Collection;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting findById(long Id){
		return (Meeting) connector.getSession().get(Meeting.class, Id);
	}

	public void createMeeting(Meeting meeting){
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void addParticipant(long meetingId, String login){
		String hql = "FROM Meeting where id =: meetingId";
		org.hibernate.query.Query<Meeting> query = connector.getSession().createQuery(hql, Meeting.class);
		query.setParameter("meetingId", meetingId);
		Meeting meeting = query.uniqueResult ();
		String hql1 = "FROM Participant where login =: login";
		org.hibernate.query.Query<Participant> query1 = connector.getSession().createQuery(hql1, Participant.class);
		query1.setParameter("login", login);
		Participant participant = query1.uniqueResult ();
		meeting.addParticipant (participant);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

}
