package br.com.diogotorresdev.moneyapi.api.event.listener;

import br.com.diogotorresdev.moneyapi.api.event.ResourceCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {

    @Override
    public void onApplicationEvent(ResourceCreatedEvent event) {
        HttpServletResponse response = event.getResponse();
        Long id = event.getId();

        addHeaderLocation(response, id);
    }

    private void addHeaderLocation(HttpServletResponse response, Long id) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

        response.setHeader("Location", uri.toASCIIString());
    }
}
