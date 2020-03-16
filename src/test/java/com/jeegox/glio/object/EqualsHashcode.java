package com.jeegox.glio.object;

import org.junit.After;
import org.junit.Before;
import static com.google.common.truth.Truth.assertThat;
import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dto.MonthDTO;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 *
 * @author j2esus
 */
public class EqualsHashcode {
    
    public EqualsHashcode() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void validate(){
        EqualsVerifier.forClass(MonthDTO.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
    
}
