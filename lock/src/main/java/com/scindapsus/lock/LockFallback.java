package com.scindapsus.lock;


import com.scindapsus.lock.exception.DistributedLockException;

/**
 * 琐失败回调
 *
 * @author wyh
 * @since 1.0
 */
@FunctionalInterface
public interface LockFallback<T> {

    /**
     * The fallback factory must be a valid spring bean.
     *
     * @param cause 报错原因
     * @return fallback处理结果
     */
    T create(Throwable cause);


    class DefaultLockFallback implements LockFallback<Void> {

        @Override
        public Void create(Throwable cause) {
            throw new DistributedLockException("Failed to acquire lock, please provide a fallback class", cause);
        }
    }
}
