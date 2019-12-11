package com.sb.echelon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Facade class to interact with the Echelon module.
 * @author Samuel Beausoleil
 *
 */

@Component
public final class Echelon {
	
	@Autowired
	private NameRegistry nameRegistry;
}
