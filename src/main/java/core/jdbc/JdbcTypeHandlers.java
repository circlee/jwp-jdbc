package core.jdbc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JdbcTypeHandlers {
	
	private final Set<JdbcTypeHandler<?>> typeHandlers = new HashSet<>();
	
	private final JdbcTypeHandler<?>[] defaultTypeHandlers = new JdbcTypeHandler[] {
			new StringJdbcTypeHandler()
		};
	
	private final Map<Class<?>, JdbcTypeHandler<?>> cache = new HashMap<>();
	
	private JdbcTypeHandlers() {
		addDefaultTypeHandlers();
	}

	public static JdbcTypeHandlers getInstance() {
		return Lazy.INSTANCE;
	}
	
	public void addTypeHandlers(JdbcTypeHandler<?> typeHandler) {
		this.typeHandlers.add(typeHandler);
	}
	
	public JdbcTypeHandler<?> getTypeHandler(Class<?> clazz) {
		
		return cache.computeIfAbsent(clazz, (key) -> {
			return typeHandlers.stream()
			.filter(handler -> handler.supports(clazz))
			.findFirst()
			.orElseThrow(() -> new RuntimeException("JdbcTypeHandler 를 찾을수 없습니다. : " + clazz));
		});
	}
	
	private void addDefaultTypeHandlers() {
		for(JdbcTypeHandler<?> typeHandler : defaultTypeHandlers) {
			this.typeHandlers.add(typeHandler);
		}
	}
	
	private static class Lazy {
		private static final JdbcTypeHandlers INSTANCE = new JdbcTypeHandlers();  
	}
}
