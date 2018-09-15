package top.infra.jackson2;

import org.springframework.core.Ordered;

/**
 * Created by zhanghaolun on 16/7/28.
 */
public interface Jackson2Customizer extends Jackson2MapperCustomizer, Jackson2BuilderCustomizer {

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
