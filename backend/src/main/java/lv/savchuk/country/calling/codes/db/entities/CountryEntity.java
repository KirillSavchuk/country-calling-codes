package lv.savchuk.country.calling.codes.db.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "country")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity {

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@Type(type = "org.hibernate.type.UUIDCharType")
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	protected UUID id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "FLAG_URL")
	private String flagUrl;

	@OneToMany(
			mappedBy = "country",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@Setter(value = AccessLevel.PRIVATE)
	private List<CountryCallingCodeEntity> callingCodes = new ArrayList<>();

	public void addCodes(List<CountryCallingCodeEntity> callingCodes) {
		callingCodes.forEach(this::addCode);
	}

	public void addCode(CountryCallingCodeEntity code) {
		callingCodes.add(code);
		code.setCountry(this);
	}

	public void removeCode(CountryCallingCodeEntity code) {
		callingCodes.remove(code);
		code.setCountry(null);
	}

}