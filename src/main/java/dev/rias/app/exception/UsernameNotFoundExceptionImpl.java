package dev.rias.app.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
/**
 * @Author Rijas
 * @Email rijas.live@gmail.com
 */
public class UsernameNotFoundExceptionImpl extends UsernameNotFoundException {

    Object[] args;

    public UsernameNotFoundExceptionImpl(String msg, Object... args) {
        super(msg);
        this.args = args;
    }

    public UsernameNotFoundExceptionImpl(String msg, Throwable cause) {
        super(msg, cause);
    }
}
