package com.jeegox.glio.dao.impl;

import com.jeegox.glio.dao.StateDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.State;

import org.springframework.stereotype.Repository;

@Repository
public class StateDAOImpl extends GenericDAOImpl<State,Integer> implements StateDAO{

}
