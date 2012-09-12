package org.jboss.forge.spec.ejb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.ejb.Stateless;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.spec.javaee.EJBFacet;
import org.jboss.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author fiorenzo pizza - fiorenzo.pizza@gmail.com
 * 
 */
@RunWith(Arquillian.class)
public class EJBPluginTest extends AbstractShellTest {

	@Test
	public void testSetup() throws Exception {

		Project project = initializeJavaProject();
		assertFalse(project.hasFacet(EJBFacet.class));
		queueInputLines("");
		getShell().execute("setup ejb");
		// Assert.assertTrue(project
		// .getFacet(DependencyFacet.class)
		// .hasDirectManagedDependency(
		// DependencyBuilder
		// .create("org.jboss.spec.javax.ejb:jboss-ejb-api_3.1_spec")));
		assertTrue(project.hasFacet(EJBFacet.class));
	}

	@Test
	public void assertStatelessAnnotation() throws Exception {
		Project project = initializeJavaProject();
		queueInputLines("", "");
		getShell().execute("setup ejb");
		getShell()
				.execute(
						"ejb new-ejb --packageAndName by.giava.FlowerEjb --type STATELESS");

		JavaSourceFacet javaClass = project.getFacet(JavaSourceFacet.class);
		JavaResource resource = javaClass.getJavaResource("by.giava.FlowerEjb");
		String content = resource.getJavaSource().toString();
		assertTrue(content.contains("@Stateless"));
	}

	@Test
	public void testNewEJbStateless() throws Exception {
		Project project = initializeJavaProject();
		queueInputLines("", "");
		getShell().execute("setup ejb");
		getShell()
				.execute(
						"ejb new-ejb --packageAndName by.giava.FlowerEjb --type STATELESS");

		JavaSourceFacet javaClass = project.getFacet(JavaSourceFacet.class);
		JavaResource resource = javaClass.getJavaResource("by.giava.FlowerEjb");

		Assert.assertTrue(resource.exists());
		JavaSource<?> source = resource.getJavaSource();
		Assert.assertNotNull(source);
		Assert.assertEquals("by.giava", source.getPackage());
		assertTrue(source.hasImport(Stateless.class));
	}

	@Test
	public void testNewEJbStateful() throws Exception {
		Project project = initializeJavaProject();
		queueInputLines("", "");
		getShell().execute("setup ejb");
		getShell()
				.execute(
						"ejb new-ejb --packageAndName by.giava.FlowerEjb --type STATEFUL");

		JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
		JavaResource resource = java.getJavaResource("by.giava.FlowerEjb");

		Assert.assertTrue(resource.exists());
		JavaSource<?> source = resource.getJavaSource();

		Assert.assertNotNull(source);
		Assert.assertEquals("by.giava", source.getPackage());
	}

	@Test
	public void testNewEJbStatelessAddExtends() throws Exception {
		Project project = initializeJavaProject();
		queueInputLines("", "");
		getShell().execute("setup ejb");
		getShell()
				.execute(
						"ejb new-ejb --packageAndName by.giava.FlowerEjb --type STATELESS");
		queueInputLines("", "");
		getShell().execute("ejb add-implements java.io.Serializable");
		JavaSourceFacet javaClass = project.getFacet(JavaSourceFacet.class);
		JavaResource resource = javaClass.getJavaResource("by.giava.FlowerEjb");

		Assert.assertTrue(resource.exists());
		JavaSource<?> source = resource.getJavaSource();
		Assert.assertNotNull(source);
		Assert.assertEquals("by.giava", source.getPackage());
		assertTrue(source.hasImport(Stateless.class));
	}
}
