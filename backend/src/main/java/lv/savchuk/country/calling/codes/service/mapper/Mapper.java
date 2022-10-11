
package lv.savchuk.country.calling.codes.service.mapper;


public interface Mapper<SOURCE, TARGET> {

	MapperStrategy<SOURCE, TARGET> getMapperStrategy();

	TARGET mapFrom(SOURCE source);

}