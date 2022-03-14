package Utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilTest {

    @Test
    void getParameter() {
        String value = PropertiesUtil.getParameter("c");
        System.out.println(value);
    }
}