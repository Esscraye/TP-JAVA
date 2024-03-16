package com.epf.rentmanager.exception;

import java.io.Serial;

public class DaoException extends Exception{

        @Serial
        private static final long serialVersionUID = 1L;

        public DaoException(String message) {
            super(message);
        }

        public DaoException(String message, Throwable cause) {
            super(message, cause);
        }

        public DaoException(Throwable cause) {
            super(cause);
        }
}
