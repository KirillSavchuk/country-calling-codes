package lv.savchuk.country.calling.codes.db.repostiory;

import lv.savchuk.country.calling.codes.db.entities.CountryCallingCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryCallingCodesRepository extends JpaRepository<CountryCallingCodeEntity, Long> {

	@Query(value = "SELECT *  FROM country_calling_code c WHERE :phoneNumber LIKE CONCAT(c.code, '%')", nativeQuery = true)
	List<CountryCallingCodeEntity> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
