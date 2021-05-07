package za.co.jaliatechnologies.buildingarestapiwithspring;

import org.junit.Assert;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@Configuration
@EnableWebMvc
@ComponentScan({"za.co.jaliatechnologies.buildingarestapiwithspring.web"})
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){


        converters.add(createXmlHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
    }
    private HttpMessageConverter<Object> createXmlHttpMessageConverter(){
        MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();

        XStreamMarshaller xstreamMarshaller = new XStreamMarshaller();
        xmlConverter.setMarshaller(xstreamMarshaller);
        xmlConverter.setUnmarshaller(xstreamMarshaller);

        return xmlConverter;
    }
    @Test
    public void testGetFoo(){

        String URI = "http://localhost, 8080/spring-boot-rest/food/{id}";
        RestTemplate resTemplate = new RestTemplate();
        Foo foo = resTemplate.getForObject(URI, Foo.class, "1");
        Assert.assertEquals(java.util.Optional.of(new Integer(1)), foo.getId());
    }
    @Test
    public void givenConsumingJson_whenReadingTheFoo_thenCorrect(){
        String URI = BASE_URI + "foos/{id}";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getMessageConverters());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<Foo> response = restTemplate.exchange(URI, HttpMethod.GET,
                entity, Foo.class, "1");
        Foo resource = response.getBody();

        assertThat(resource,notNullValue);
    }
    private List<HttpMessageConverter<?>>getMessageConverters(){
        XStreamMarshaller marshaller = new XStreamMarshaller();
        MarshallingHttpMessageConverter marshallingHttpMessageConverter
                = new MarshallingHttpMessageConverter(marshaller);

        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(marshallingHttpMessageConverter);
        return converters;
    }

    public void givenConsumingXml_whenWritingTheFoo_thenCorrect(){
        String URI = BASE_URI + "foos/{id}";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getMessageConverters());

        Foo resource = new Foo("jason", 4);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType((MediaType.APPLICATION_XML));
        HttpEntity<Foo> entity = new HttpEntity<Foo>(resource, headers);

        ResponseEntity<Foo> response = restTemplate.exchange(
                URI, HttpMethod.PUT, entity, Foo.class, resource.getId());
        Foo fooResponse = response.getBody();

        Assert.assertEquals(resource.getId(), fooResponse.getId());
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
