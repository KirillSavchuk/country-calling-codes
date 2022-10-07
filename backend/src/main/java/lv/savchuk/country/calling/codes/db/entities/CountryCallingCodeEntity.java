package lv.savchuk.country.calling.codes.db.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "country_calling_code")
@NoArgsConstructor
@AllArgsConstructor
public class CountryCallingCodeEntity {

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	@Type(type = "org.hibernate.type.UUIDCharType")
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	protected UUID id;

	@Column(name = "CODE")
	private int code;

	@ManyToOne(fetch = FetchType.LAZY)
	private CountryEntity country;

	public CountryCallingCodeEntity(int code) {
		this.code = code;
	}

}