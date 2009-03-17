/*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package test.annotated.fields;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Visa {
	@OneToMany(mappedBy="country")
	private Country[] countries;
	
	public Visa(Country[] countries){
		this.countries = countries;
	}

}
