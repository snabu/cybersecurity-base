package sec.project.config;


import org.apache.catalina.Context;
import org.apache.catalina.SessionIdGenerator;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Arrays;

@Configuration
public class TomcatCustomConfiguration {
    private static int sessionid = 0;
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.getManager().setSessionIdGenerator(sessionIdGenerator());
                context.setUseHttpOnly(false);
            }
        };
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setTomcatContextCustomizers(Arrays.asList(new TomcatContextCustomizer[]{tomcatContextCustomizer()}));
        return factory;
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new StandardSessionIdGenerator() {
            @Override
            public String generateSessionId(String route) {
                sessionid++;
                return Integer.toString(sessionid);
            }

        };
    }
}
