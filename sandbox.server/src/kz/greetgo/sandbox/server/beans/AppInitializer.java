package kz.greetgo.sandbox.server.beans;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.db.util.App;
import kz.greetgo.sandbox.db.util.LiquibaseManager;

import javax.servlet.ServletContext;
import java.nio.charset.Charset;

@Bean
public class AppInitializer {

  public BeanGetter<LiquibaseManager> liquibaseManager;

  public BeanGetter<ControllerServlet> controllerServlet;

  public BeanGetter<Utf8AndTraceResetFilter> utf8AndTraceResetFilter;

  public void initialize(ServletContext ctx) throws Exception {
    System.out.println(Charset.defaultCharset());

    if (!App.do_not_run_liquibase_on_deploy_war().exists()) {
      liquibaseManager.get().apply();
    }

    utf8AndTraceResetFilter.get().register(ctx);

    controllerServlet.get().register(ctx);
  }
}
