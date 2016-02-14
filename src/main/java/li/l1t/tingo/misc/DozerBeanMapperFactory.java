package li.l1t.tingo.misc;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * Factory that creates Dozer bean mappers for autowiring.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-02-14
 */
@Component
public class DozerBeanMapperFactory implements FactoryBean<DozerBeanMapper> {
    private static final DozerBeanMapper DOZER_BEAN_MAPPER = new DozerBeanMapper();

    @Override
    public DozerBeanMapper getObject() throws Exception {
        return DOZER_BEAN_MAPPER;
    }

    @Override
    public Class<?> getObjectType() {
        return DozerBeanMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
