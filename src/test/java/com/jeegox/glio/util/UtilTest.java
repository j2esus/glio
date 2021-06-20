package com.jeegox.glio.util;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

public class UtilTest {

    @Test
    public void encodeSha256_letters(){
        String stringSha256 = Util.encodeSha256("test");
        assertThat(stringSha256)
                .isEqualTo("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
    }
    
    @Test
    public void encodeSha256_numbers() {
        String stringSha256 = Util.encodeSha256("123456789");
        assertThat(stringSha256)
                .isEqualTo("15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225");
    }
    
    @Test
    public void encodeSha256_lettersAndNumbers() {
        String stringSha256 = Util.encodeSha256("test123456");
        assertThat(stringSha256)
                .isEqualTo("85777f270ad7cf2a790981bbae3c4e484a1dc55e24a77390d692fbf1cffa12fa");
    }
}
