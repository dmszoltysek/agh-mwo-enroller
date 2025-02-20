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
	public Collection<Meeting> getAllSortedByTitle() {
		String hql = "FROM Meeting ORDER BY title ASC";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting findById(long Id){
		return (Meeting) connector.getSession().get(Meeting.class, Id);
	}

	public void createMeeting(Meeting meeting){
		System.out.println (meeting.getId ());
		System.out.println (meeting.getTitle());
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void deleteMeeting(Meeting meeting){
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}

	public void addParticipant(Meeting meeting, Participant participant){
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.addParticipant (participant);
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void deleteParticipant(Meeting meeting, Participant participant){
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.removeParticipant (participant);
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void update(Meeting meeting){
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
	}


}
