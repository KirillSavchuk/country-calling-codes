package lv.savchuk.country.calling.codes.db.repostiory;

import lv.savchuk.country.calling.codes.db.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}