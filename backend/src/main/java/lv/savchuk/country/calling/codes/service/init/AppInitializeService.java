package lv.savchuk.country.calling.codes.service.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppInitializeService implements ApplicationRunner {

	private final List<AppInitializer> initializers;

	@Override
	public void run(ApplicationArguments args) {
		log.info("Application started with option names: {}", args.getOptionNames());
		initializers.stream()
				.filter(initializer -> initializer.shouldRun(args))
				.forEach(initializer -> {
					log.info("Running '{}' initializer...", initializer.getClass().getSimpleName());
					initializer.run(args);
				});
		log.info("Finished application initialization process.");
	}

}