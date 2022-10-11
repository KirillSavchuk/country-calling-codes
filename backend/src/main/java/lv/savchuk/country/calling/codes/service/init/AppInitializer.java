package lv.savchuk.country.calling.codes.service.init;


import org.springframework.boot.ApplicationArguments;

public interface AppInitializer {

    default boolean shouldRun(ApplicationArguments args) {
        return true;
    }

    void run(ApplicationArguments args);

}
