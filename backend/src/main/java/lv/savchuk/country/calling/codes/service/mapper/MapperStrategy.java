package lv.savchuk.country.calling.codes.service.mapper;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class MapperStrategy<SOURCE, TARGET> {

	private final Class<SOURCE> source;
	private final Class<TARGET> target;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MapperStrategy<?, ?> that = (MapperStrategy<?, ?>) o;
		return Objects.equals(source, that.source) && Objects.equals(target, that.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target);
	}

}