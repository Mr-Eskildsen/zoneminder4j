/**
 * Copyright (c) 2014-2016 openHAB UG (haftungsbeschraenkt) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package name.eskildsen.zoneminder.event;

public enum ZoneMinderEventAction {
    ON("on"),
    OFF("off");

    private final String fieldDescription;

    private ZoneMinderEventAction(String value) {
        fieldDescription = value;
    }

    @Override
    public String toString() {
        return fieldDescription;
    }

    public static ZoneMinderEventAction getEnum(String value) {
        return ZoneMinderEventAction.valueOf(value.toUpperCase());
    }
}
