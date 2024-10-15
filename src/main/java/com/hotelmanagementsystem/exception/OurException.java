package com.hotelmanagementsystem.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OurException extends RuntimeException {
	private String resourceName;

    public OurException(String resourceName) {
        super(String.format("%s",resourceName));
        this.resourceName=resourceName;
    }
}
