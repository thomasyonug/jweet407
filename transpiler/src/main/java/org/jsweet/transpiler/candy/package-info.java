/* 
 * JSweet transpiler - http://www.jsweet.org
 * Copyright (C) 2015 CINCHEO SAS <renaud.pawlak@cincheo.fr>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
/**
 * This package contains the candies processing implementation.
 * 
 * <p>
 * The candies processor extract the TypeScript definition files from the
 * candies found in the classpath. JSweet will use the generated definitions for
 * the phase 2 or the transpilation (TypeScript -> JavaScript) and ensure that
 * the generated TypeScript is well-typed.
 * 
 * @author Louis Grignon
 * @author Johann Sorel
 */
package org.jsweet.transpiler.candy;
