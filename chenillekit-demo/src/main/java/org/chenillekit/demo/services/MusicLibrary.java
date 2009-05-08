/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.demo.services;

import java.util.List;

import org.chenillekit.demo.data.Track;

public interface MusicLibrary
{
    /**
     * Gets a track by its unique id.
     *
     * @param id of track to retrieve
     *
     * @return the Track
     *
     * @throws IllegalArgumentException if no such track exists
     */
    Track getById(long id);

    /**
     * Provides a list of all tracks in an indeterminate order.
     */
    List<Track> getTracks();

    /**
     * Performs a case-insensitive search, finding all tracks whose title contains the input string (ignoring case).
     *
     * @param title a partial title
     *
     * @return a list of all matches
     */
    List<Track> findByMatchingTitle(String title);
}
