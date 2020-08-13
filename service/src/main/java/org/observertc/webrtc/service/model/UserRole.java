package org.observertc.webrtc.service.model;

import org.observertc.webrtc.service.jooq.enums.UsersRole;

public enum UserRole {
	CUSTOMER,
	ADMIN;

	public static final String ADMIN_ROLE = "ADMIN";
	public static final String CUSTOMER_ROLE = "CUSTOMER";

	public static UserRole mapFromDB(UsersRole role) throws IllegalStateException {
		switch (role) {
			case admin:
				return UserRole.ADMIN;
			case client:
				return UserRole.CUSTOMER;
		}
		throw new IllegalStateException("A user role given as parameter is not defined");
	}

	public static UsersRole mapToDB(UserRole role) throws IllegalStateException {
		switch (role) {
			case ADMIN:
				return UsersRole.admin;
			case CUSTOMER:
				return UsersRole.client;
		}
		throw new IllegalStateException("A user role given as parameter is not defined");
	}

	public static UserRole mapFromString(String role) throws IllegalStateException {
		switch (role.toUpperCase()) {
			case ADMIN_ROLE:
				return UserRole.ADMIN;
			case CUSTOMER_ROLE:
				return UserRole.CUSTOMER;
		}
		throw new IllegalStateException("A user role given as parameter is not defined");
	}

	@Override
	public String toString() {
		switch (this) {
			case ADMIN:
				return ADMIN_ROLE;
			case CUSTOMER:
				return CUSTOMER_ROLE;
		}
		throw new IllegalStateException("The role intended to stringified does not exists");
	}
}
