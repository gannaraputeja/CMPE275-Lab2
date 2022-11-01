package edu.sjsu.cmpe275.lab2.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
//import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
public class AppConfiguration {

    /*@Bean
    public HttpMessageConverter<Object> createXMLHttpMessageConverter() {
        MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();

        XStreamMarshaller xstreamMarshaller = new XStreamMarshaller();
        xstreamMarshaller.getXStream().ignoreUnknownElements();
        xmlConverter.setMarshaller(xstreamMarshaller);
        xmlConverter.setUnmarshaller(xstreamMarshaller);

        return xmlConverter;
    }*/

    /*@Bean
    public MappingJackson2XmlHttpMessageConverter createMappingJackson2XmlHttpMessageConverter() {
        return new MappingJackson2XmlHttpMessageConverter();
    }*/

    /*@Bean
    public MappingJackson2HttpMessageConverter createMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(new ObjectMapper());
        return jsonConverter;
    }*/

}
