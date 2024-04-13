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
package org.jsweet.transpiler;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * This class describes a module import.
 * 
 * @author Renaud Pawlak
 */
public class ModuleImportDescriptor {
    public ModuleImportDescriptor(PackageElement targetPackage, String importedName, String pathToImportedClass) {
        super();
        this.targetPackage = targetPackage;
        this.importedName = importedName;
        this.pathToImportedClass = pathToImportedClass;
    }
    
    public ModuleImportDescriptor(String importedName, String pathToImportedClass) {
        super();
        this.importedName = importedName;
        this.pathToImportedClass = pathToImportedClass;
    }

    public ModuleImportDescriptor(boolean direct, PackageElement targetPackage, String importedName,
            String pathToImportedClass) {
        this(targetPackage, importedName, pathToImportedClass);
        this.direct = direct;
    }
    
    public ModuleImportDescriptor(boolean direct, PackageElement targetPackage, String importedName,
            String pathToImportedClass, TypeElement importedClass) {
        this(direct, targetPackage, importedName, pathToImportedClass);
        this.importedClass = importedClass;
    }

    private boolean direct = false;
    private PackageElement targetPackage;
    private String importedName;
    private String pathToImportedClass;
    private TypeElement importedClass;

    /**
     * Returns the imported class if any.
     */
    public TypeElement getImportedClass() {
        return importedClass;
    }
    
    /**
     * Gets the package of the element being imported.
     */
    public PackageElement getTargetPackage() {
        return targetPackage;
    }

    /**
     * Gets the name of the import.
     */
    public String getImportedName() {
        return importedName;
    }

    /**
     * Gets the path to the imported class.
     */
    public String getPathToImportedClass() {
        return pathToImportedClass;
    }

    /**
     * True for a direct import.
     */
    public boolean isDirect() {
        return direct;
    }
}