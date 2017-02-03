package rest;

import config.Config;
import config.ConfigMapping;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named("Bean Resource")
@Path("/")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
@RequestScoped
public class TestRestController {

	@Inject
	@ConfigMapping("some.another")
	private Config<TestEntity> configB;

	@Inject
	private Config<TestEntity> configA;

	@Inject
	@ConfigMapping("random.key")
	private String test;

	// ToDo Collections / Maps
	// ToDo Enum

	@Inject
	private Map<String, TestEntity> configMap;

	@Inject
	@ConfigMapping(value = "random.value", required = false)
	private int x;

	@GET
	@Path("/")
	public List<TestEntity> fetchAll() {
		List<TestEntity> list = new ArrayList<>();
		list.add(configA.get());
		list.add(configB.get());

		TestEntity t = new TestEntity();
		t.setOf(test);
		t.setValue(x);
		list.add(t);
		return list;
	}
}
