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

package org.chenillekit.demo.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.Accordion;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id$
 */
public class AccordionDemo
{
    @Property
    private String[] _subjects = {"Subject 1", "Subject 2", "Subject 3", "Subject 4", "Subject 5"};

    @Property
    private String[] _details = {"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Quisque consequat purus sed nulla.\n" +
            "Proin nunc nunc, vehicula eu, sollicitudin sit amet, condimentum pulvinar, eros. Praesent dui.\n" +
            "Pellentesque porta, magna sit amet tristique congue, ligula magna ornare ligula, vitae lobortis\n" +
            "dolor lorem vitae purus. Nam convallis turpis non augue. Class aptent taciti sociosqu ad litora\n" +
            "torquent per conubia nostra, per inceptos hymenaeos. Maecenas sit amet mi in nisi laoreet\n" +
            "consectetuer. Quisque orci sem, tincidunt quis, adipiscing sodales, sagittis egestas, leo.\n" +
            "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\n" +
            "Vestibulum sed turpis in arcu porta consectetuer. Duis arcu erat, porta ut, convallis et, pulvinar\n" +
            "a, quam. Aliquam nunc. Pellentesque interdum nibh ac dolor. Vivamus auctor consequat sapien.\n" +
            "In odio metus, hendrerit at, ornare in, pellentesque quis, nunc. Aenean ultricies est id lacus.\n" +
            "Nam neque. Maecenas cursus rutrum magna. Etiam lacus. Mauris non nunc.",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Quisque consequat purus sed nulla.\n" +
                    "Proin nunc nunc, vehicula eu, sollicitudin sit amet, condimentum pulvinar, eros. Praesent dui.\n" +
                    "Pellentesque porta, magna sit amet tristique congue, ligula magna ornare ligula, vitae lobortis\n" +
                    "dolor lorem vitae purus. Nam convallis turpis non augue. Class aptent taciti sociosqu ad litora\n" +
                    "torquent per conubia nostra, per inceptos hymenaeos. Maecenas sit amet mi in nisi laoreet\n" +
                    "consectetuer. Quisque orci sem, tincidunt quis, adipiscing sodales, sagittis egestas, leo.\n" +
                    "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\n" +
                    "Vestibulum sed turpis in arcu porta consectetuer. Duis arcu erat, porta ut, convallis et, pulvinar\n" +
                    "a, quam. Aliquam nunc. Pellentesque interdum nibh ac dolor. Vivamus auctor consequat sapien.\n" +
                    "In odio metus, hendrerit at, ornare in, pellentesque quis, nunc. Aenean ultricies est id lacus.\n" +
                    "Nam neque. Maecenas cursus rutrum magna. Etiam lacus. Mauris non nunc.",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Quisque consequat purus sed nulla.\n" +
                    "Proin nunc nunc, vehicula eu, sollicitudin sit amet, condimentum pulvinar, eros. Praesent dui.\n" +
                    "Pellentesque porta, magna sit amet tristique congue, ligula magna ornare ligula, vitae lobortis\n" +
                    "dolor lorem vitae purus. Nam convallis turpis non augue. Class aptent taciti sociosqu ad litora\n" +
                    "torquent per conubia nostra, per inceptos hymenaeos. Maecenas sit amet mi in nisi laoreet\n" +
                    "consectetuer. Quisque orci sem, tincidunt quis, adipiscing sodales, sagittis egestas, leo.\n" +
                    "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\n" +
                    "Vestibulum sed turpis in arcu porta consectetuer. Duis arcu erat, porta ut, convallis et, pulvinar\n" +
                    "a, quam. Aliquam nunc. Pellentesque interdum nibh ac dolor. Vivamus auctor consequat sapien.\n" +
                    "In odio metus, hendrerit at, ornare in, pellentesque quis, nunc. Aenean ultricies est id lacus.\n" +
                    "Nam neque. Maecenas cursus rutrum magna. Etiam lacus. Mauris non nunc.",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Quisque consequat purus sed nulla.\n" +
                    "Proin nunc nunc, vehicula eu, sollicitudin sit amet, condimentum pulvinar, eros. Praesent dui.\n" +
                    "Pellentesque porta, magna sit amet tristique congue, ligula magna ornare ligula, vitae lobortis\n" +
                    "dolor lorem vitae purus. Nam convallis turpis non augue. Class aptent taciti sociosqu ad litora\n" +
                    "torquent per conubia nostra, per inceptos hymenaeos. Maecenas sit amet mi in nisi laoreet\n" +
                    "consectetuer. Quisque orci sem, tincidunt quis, adipiscing sodales, sagittis egestas, leo.\n" +
                    "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\n" +
                    "Vestibulum sed turpis in arcu porta consectetuer. Duis arcu erat, porta ut, convallis et, pulvinar\n" +
                    "a, quam. Aliquam nunc. Pellentesque interdum nibh ac dolor. Vivamus auctor consequat sapien.\n" +
                    "In odio metus, hendrerit at, ornare in, pellentesque quis, nunc. Aenean ultricies est id lacus.\n" +
                    "Nam neque. Maecenas cursus rutrum magna. Etiam lacus. Mauris non nunc.",
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Quisque consequat purus sed nulla.\n" +
                    "Proin nunc nunc, vehicula eu, sollicitudin sit amet, condimentum pulvinar, eros. Praesent dui.\n" +
                    "Pellentesque porta, magna sit amet tristique congue, ligula magna ornare ligula, vitae lobortis\n" +
                    "dolor lorem vitae purus. Nam convallis turpis non augue. Class aptent taciti sociosqu ad litora\n" +
                    "torquent per conubia nostra, per inceptos hymenaeos. Maecenas sit amet mi in nisi laoreet\n" +
                    "consectetuer. Quisque orci sem, tincidunt quis, adipiscing sodales, sagittis egestas, leo.\n" +
                    "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\n" +
                    "Vestibulum sed turpis in arcu porta consectetuer. Duis arcu erat, porta ut, convallis et, pulvinar\n" +
                    "a, quam. Aliquam nunc. Pellentesque interdum nibh ac dolor. Vivamus auctor consequat sapien.\n" +
                    "In odio metus, hendrerit at, ornare in, pellentesque quis, nunc. Aenean ultricies est id lacus.\n" +
                    "Nam neque. Maecenas cursus rutrum magna. Etiam lacus. Mauris non nunc."};

    @Component(parameters = {"menuName=demo"})
    private LeftSideMenu menu;

    @Component(parameters = {"subjects=subjects", "details=details"})
    private Accordion _accordion1;

    @Component(parameters = {"subjects=subjects", "details=details", "duration=literal:1.0"})
    private Accordion _accordion2;
}