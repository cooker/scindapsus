package com.scindapsus.ds.tx;

import com.scindapsus.ds.annotation.WithDataSource;
import com.scindapsus.ds.mapper.CarMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 模拟嵌套事务
 * <p>
 * </p>ds组件上下文切换是用aop实现的，因为aop的特性原因，不支持同类中方法嵌套调用。直接嵌套调用会单纯认为是方法调用，aop辐射不到，
 * 所以可以用AopContext、自己注入自己、新建另一个类调用等方式来解决。
 * <p>例子这里使用AopContext来调用自己来保证都被aop识别（使用aopContext来调用本类其他方法需要设置aop的exposeProxy为true，不然会抛错）
 *
 * @author wyh
 * @date 2022/8/24 14:37
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@Service
public class LocalTxCarService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private final CarMapper carMapper;

    public LocalTxCarService(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    @WithDataSource(value = "second", tx = true)
    public void ds2() {
        String format = sdf.format(new Date());
        carMapper.update(3L, "ds2_" + format);
        carMapper.update(4L, "ds2_" + format);
    }

    @WithDataSource(value = "second", tx = true)
    public void ds2Throw() {
        String format = sdf.format(new Date());
        carMapper.update(3L, "ds2_" + format);
        int i = 1 / 0;
        carMapper.update(4L, "ds2_" + format);
    }

    @WithDataSource(value = "first", tx = true)
    public void nested() {
        String format = sdf.format(new Date());
        carMapper.update(1L, "ds1_" + format);
        carMapper.update(2L, "ds1_" + format);
        ((LocalTxCarService) AopContext.currentProxy()).ds2();
    }

    @WithDataSource(value = "first", tx = true)
    public void nestedThrow() {
        String format = sdf.format(new Date());
        carMapper.update(1L, "ds1_" + format);
        carMapper.update(2L, "ds1_" + format);
        ((LocalTxCarService) AopContext.currentProxy()).ds2Throw();
    }
}
