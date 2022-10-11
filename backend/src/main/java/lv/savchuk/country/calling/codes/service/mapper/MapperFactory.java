package lv.savchuk.country.calling.codes.service.mapper;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.stream.Collectors.toUnmodifiableMap;

@Service
public class MapperFactory {

	private final Map<MapperStrategy<?, ?>, Mapper<?, ?>> mappers;

	public MapperFactory(List<Mapper<?, ?>> mappers) {
		this.mappers = mappers.stream().collect(toUnmodifiableMap(Mapper::getMapperStrategy, Function.identity()));
	}

	@SuppressWarnings("unchecked")
	public <SOURCE, TARGET> Mapper<SOURCE, TARGET> getMapperFor(Class<SOURCE> sourceCls, Class<TARGET> targetCls) {
		final MapperStrategy<SOURCE, TARGET> mapperStrategy = new MapperStrategy<>(sourceCls, targetCls);
		if (mappers.containsKey(mapperStrategy)) {
			return (Mapper<SOURCE, TARGET>) mappers.get(mapperStrategy);
		}
		throw new NotImplementedException(format("Mapper is not implemented for strategy: SOURCE=%s, TARGET=%s.",
				mapperStrategy.getSource().getSimpleName(), mapperStrategy.getTarget().getSimpleName()));
	}

}