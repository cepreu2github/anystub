package org.anystub.it;

import org.anystub.Base;
import org.anystub.mgmt.BaseManagerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

@Configuration
public class DefaultConfiguration {
    @Bean
    public String externalSystemUrl() {
        return "http://localhost:8080";
    }



    @Bean
    Base base() {
        return BaseManagerFactory
                .getBaseManager()
                .getBase();
    }

    @Bean
    @Primary
    public SourceSystem sourceSystemTest(Base base) {
        return new SourceSystem("http://localhost:8080") {
            @Override
            public String get() throws IOException {
                return base.request("root");
            }

            /**
             * pay attention: the result of the function depends on
             * internal state of the object, which changed by invocation of the function
             * @param digit should has the same mean as in super class
             * @return from upstream or from
             */
            @Override
            public Integer rand(int digit) {
                return Integer.valueOf(
                        base.request("rand", Integer.toString(digit)));
            }
        };
    }

}
