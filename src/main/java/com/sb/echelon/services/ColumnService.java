package com.sb.echelon.services;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.sb.echelon.ColType;
import com.sb.echelon.Column;

@Service
public class ColumnService {

	/**
	 * Gets the type of the column if specified by the annotated field.
	 * First checks the type annotation field, then overrides it by customType if it
	 * is set.
	 * 
	 * @param col
	 * @return the column type or null if the annotation does not describe a
	 *         specific type.
	 */
	public @Nullable String columnType(@Nullable Column col) {
		String type = null;
		if (col != null) {
			if (col.type() != ColType.ECHELON_GENERATED)
				type = col.type().sql;
			if (!col.customType().equals(Column.USE_TYPE))
				type = col.customType();
		}
		return type;
	}
}
