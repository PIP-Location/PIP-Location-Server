package kr.co.pinpick;

import kr.co.pinpick.common.argumenthandler.Entity;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication
public class PinpickApplication {

	static {
		SpringDocUtils.getConfig()
				.addAnnotationsToIgnore(Entity.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(PinpickApplication.class, args);
	}

}
