package com.xsn.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
//当spring容器内没有TomcatEmbeddedServletContainerFactory这个bean时，会把此bean加载进spring容器内
@Component
public class WebServerConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory configurableWebServerFactory) {
        //使用对应工厂类提供给我们的接口定制化我们的tomcat connector
        ((TomcatServletWebServerFactory)configurableWebServerFactory) .addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
              Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
              //定制化keepaliveTimeOut,设置30s内没有请求则服务端自动断开keepalive
                protocol.setKeepAliveTimeout(30000);
                //最大请求10000之后断开
                protocol.setMaxKeepAliveRequests(10000);
            }
        });
    }
}
