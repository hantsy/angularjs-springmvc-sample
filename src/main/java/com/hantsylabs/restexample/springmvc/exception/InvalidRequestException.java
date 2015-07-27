/*
 * Copyright 2015 Hantsy Bai<hantsy@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hantsylabs.restexample.springmvc.exception;

import org.springframework.validation.BindingResult;

/**
 *
 * @author Hantsy Bai<hantsy@gmail.com>
 */
public class InvalidRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    BindingResult errors;

    public InvalidRequestException(BindingResult errors) {
        this.errors = errors;
    }

    public BindingResult getErrors() {
        return this.errors;
    }
}
