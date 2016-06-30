package io.summerx.framework.zookeeper.curator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringValueResolver;

import java.util.*;

/**
 *  推荐[disconf]https://github.com/knightliao/disconf
 */
@Deprecated
public class ReloadablePropertyPlaceholderConfigurer extends CuratorZooKeeperConfigurer implements ApplicationContextAware {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ApplicationContext applicationContext;

	private String currentBeanName;

    private String currentPropertyName;

	private Map<String, List<String>> reloadBeanNameMap = new HashMap<String, List<String>>();

    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        logger.info("ReloadablePropertyPlaceholderConfigurer.processProperties(ConfigurableListableBeanFactory, Properties)");
        super.processProperties(beanFactoryToProcess, props);
    }

	@Override
	protected void doProcessProperties(ConfigurableListableBeanFactory bf, StringValueResolver valueResolver) {
		// 使用自定义的BeanDefinitionVisitor
		BeanDefinitionVisitor visitor = new ReloadableBeanDefinitionVisitor(valueResolver);

		for (String name : bf.getBeanDefinitionNames()) {
			currentBeanName = name;
			BeanDefinition bd = bf.getBeanDefinition(name);
			try {
                logger.info("visitor.visitBeanDefinition(" + bd.getBeanClassName() + ")");
				visitor.visitBeanDefinition(bd);
			} catch (Exception ex) {
				throw new BeanDefinitionStoreException(bd.getResourceDescription(), name, ex.getMessage(), ex);
			} finally {
				currentBeanName = null;
			}
		}

		bf.resolveAliases(valueResolver);
		bf.addEmbeddedValueResolver(valueResolver);
	}

    protected void dataChanged(String placeholder) {
        // FIXME 重新加载所有配置?

        // 判断placeholder是否变化

        // 获取受影响的属性

        // 重新设置受影响的属性




        // 需要重新初始化的bean
//        List<String> reloadBeanNameList = reloadBeanNameMap.get(name);
//        if (reloadBeanNameList == null || reloadBeanNameList.isEmpty()) {
//            return;
//        }
//
//        for (String reloadBeanName : reloadBeanNameList) {
//            if (!(applicationContext instanceof ConfigurableApplicationContext)) {
//                return;
//            }
//            ConfigurableApplicationContext cac = (ConfigurableApplicationContext) applicationContext;
//            ConfigurableListableBeanFactory clbf = cac.getBeanFactory();
//            BeanDefinition bd = clbf.getBeanDefinition(reloadBeanName);
//            if (clbf instanceof BeanDefinitionRegistry) {
//                logger.info("===================================================================================");
//                logger.info("reload: " + reloadBeanName);
//                logger.info("===================================================================================");
//                BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) clbf;
//                bdr.removeBeanDefinition(reloadBeanName);
//                bdr.registerBeanDefinition(reloadBeanName, bd);
//            }
//        }
    }

	/*
	 * 一个自定义的BeanDefinitionVisitor，可以记录需要reload的BeanDefinition信息
	 */
	private class ReloadableBeanDefinitionVisitor extends BeanDefinitionVisitor {

        Properties props;

		public ReloadableBeanDefinitionVisitor(StringValueResolver valueResolver) {
			super(valueResolver);
		}

		@Override
		public void visitBeanDefinition(BeanDefinition beanDefinition) {
			super.visitBeanDefinition(beanDefinition);
		}

		@Override
		protected void visitPropertyValues(MutablePropertyValues pvs) {
//			super.visitPropertyValues(pvs);
            PropertyValue[] pvArray = pvs.getPropertyValues();
            for (PropertyValue pv : pvArray) {
                Object newVal = resolveValue(pv.getValue());
                if (!ObjectUtils.nullSafeEquals(newVal, pv.getValue())) {
                    pvs.add(pv.getName(), newVal);
                }
            }
		}

		@Override
		protected Object resolveValue(Object value) {
			return super.resolveValue(value);
		}

		@Override
		protected String resolveStringValue(String strVal) throws BeansException {

            String parsedValue = super.resolveStringValue(strVal);

            // 记录当前BeanName，PropertyName，表达式，使用到的placeholder和表达式的值
            // 当placeholder变化时，重新计算表达式的值，如果发生了变化则重新设置Bean的属性值
            DynamicProperty dynamic = null;
            StringBuffer buf = new StringBuffer(strVal);
            int startIndex = strVal.indexOf(placeholderPrefix);
            while (startIndex != -1) {
                int endIndex = buf.toString().indexOf(placeholderSuffix, startIndex + placeholderPrefix.length());
                if (endIndex != -1) {
                    if (currentBeanName != null && currentPropertyName != null) {
                        String placeholder = buf.substring(startIndex + placeholderPrefix.length(), endIndex);
                        if (dynamic == null) {
                            dynamic = new DynamicProperty(currentBeanName, currentPropertyName, strVal, parsedValue);
                        }
                        dynamic.addPlaceholder(placeholder);
                    } else {
                        logger.warn("dynamic property outside bean property value - ignored: " + strVal);
                    }
                    startIndex = endIndex + placeholderSuffix.length();
                    startIndex = strVal.indexOf(placeholderPrefix, startIndex);
                } else {
                    startIndex = -1;
                }
            }

			return parsedValue;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

    /**
     * 动态属性封装对象
     */
    public static class DynamicProperty {

        /**
         * Bean Name
         */
        final String beanName;

        /**
         * 属性名
         */
        final String propertyName;

        /**
         * 表达式
         */
        final String expression;

        /**
         * 表达式的值
         */
        final String expressionValue;

        /**
         * placeholder名称为key，placeholder值为value
         */
        Set<String> placeholders = new HashSet<>();

        public DynamicProperty(String beanName, String propertyName, String expression, String expressionValue) {
            this.beanName = beanName;
            this.propertyName = propertyName;
            this.expression = expression;
            this.expressionValue = expressionValue;
        }


        public void addPlaceholder(String placeholder) {
            placeholders.add(placeholder);
        }

        public String getExpression() {
            return expression;
        }

        public String getBeanName() {
            return beanName;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public String getExpressionValue() {
            return expressionValue;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final DynamicProperty that = (DynamicProperty) o;

            if (beanName != null ? !beanName.equals(that.beanName) : that.beanName != null) {
                return false;
            }
            if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) {
                return false;
            }

            return true;
        }

        public int hashCode() {
            int result;
            result = (beanName != null ? beanName.hashCode() : 0);
            result = 29 * result + (propertyName != null ? propertyName.hashCode() : 0);
            return result;
        }
    }
}
