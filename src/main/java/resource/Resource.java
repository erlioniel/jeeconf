package resource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Named("Bean Resource")
@Path("/")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RequestScoped
public class Resource {

	@Inject
	private Configuration<ConfigurationEntity> config;

	@GET
	@Path("/")
	public List<ConfigurationEntity> fetchAll() {
		List<ConfigurationEntity> list = new ArrayList<>();
		list.add(config.get());
		return list;
	}
}
