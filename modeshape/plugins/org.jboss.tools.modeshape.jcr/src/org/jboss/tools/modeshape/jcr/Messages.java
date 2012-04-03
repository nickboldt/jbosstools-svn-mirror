/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr;

import org.eclipse.osgi.util.NLS;

/**
 * 
 */
public final class Messages extends NLS {

    /**
     * The name for the child definition's name property.
     */
    public static String childDefinitionName;

    /**
     * A message indicating the CND has no namespace or node type definitions.
     */
    public static String cndHasNoNamespacesOrNodeTypeDefinitions;

    /**
     * A message indicating a child node definition has a default type but that has been marked as a variant. One parameter, the
     * child node definition name, is required.
     */
    public static String defaultTypeExistsButMarkedAsVariant;

    /**
     * The name for the child definition's default type property.
     */
    public static String defaultTypeName;

    /**
     * The name for the property definition's default value property.
     */
    public static String defaultValue;

    /**
     * A message indicating a property definition has default values but that property has been marked as a variant. One parameter,
     * the property definition name, is required.
     */
    public static String defaultValuesExistButMarkedAsVariant;

    /**
     * A message indicating more than one child node definition has the same name. Two parameters, the property definition name and
     * the child node name, are required.
     */
    public static String duplicateChildNodeDefinitionName;

    /**
     * A message indicating a property definition has duplicate default values. Two parameters, the property definition name and the
     * duplicate value, are required.
     */
    public static String duplicateDefaultValue;

    /**
     * A message indicating multiple namespace mappings have the same prefix. One parameter, the duplicate prefix, is required.
     */
    public static String duplicateNamespacePrefix;

    /**
     * A message indicating multiple namespace mappings have the same prefix. One parameter, the prefix, is required.
     */
    public static String duplicateNamespaceUri;

    /**
     * A message indicating multiple node type definitions with the same name exist. One parameter, the duplicate node type
     * definition name, is required.
     */
    public static String duplicateNodeTypeDefinitionName;

    /**
     * A message indicating multiple property definitions with the same name exist. One parameter, the duplicate property definition
     * name, is required.
     */
    public static String duplicatePropertyDefinitionName;

    /**
     * A message indicating duplicate qualified names exist. Two parameters, the type of qualified name and the duplicate qualified
     * name, are required.
     */
    public static String duplicateQualifiedName;

    /**
     * A message indicating duplicate query operators of a property definition exist. The parameters of property definition name and
     * the duplicate query operator are required.
     */
    public static String duplicateQueryOperator;

    /**
     * A message indicating duplicate required types of a child node definition exist. The parameters of child node definition name
     * and the duplicate required type name are required.
     */
    public static String duplicateRequiredType;

    /**
     * A message indicating duplicate super types of a node type definition exist. The parameters of node type definition name and
     * the duplicate super type name are required.
     */
    public static String duplicateSuperType;

    /**
     * A message indicating duplicate value constraints of a property definition exist. The parameters of property definition name
     * and the duplicate value constraint are required.
     */
    public static String duplicateValueConstraint;

    /**
     * A message indicating the property definition is missing default values. One parameter, the property definition name, is
     * required.
     */
    public static String emptyDefaultValues;

    /**
     * A message indicating a name of a property definition, child node definition, or node type definition is missing. One
     * parameter, the string identifying the name, is required.
     */
    public static String emptyLocalName;

    /**
     * A message indicating a property definition has an empty query operator. One parameter, the property definition name, is
     * required.
     */
    public static String emptyQueryOperator;

    /**
     * A message indicating a property definition is missing query operators. One parameter, the property definition name, is
     * required.
     */
    public static String emptyQueryOperators;

    /**
     * A message indicating a child node definition is missing required types. One parameter, the property definition name, is
     * required.
     */
    public static String emptyRequiredTypes;

    /**
     * A message indicating a node type definition is missing super types. One parameter, the node type definition name, is
     * required.
     */
    public static String emptySuperTypes;

    /**
     * A message indicating the unqualified name part of a qualified name is empty. One parameter, the name or type of qualified
     * name, is required.
     */
    public static String emptyUnqualifiedName;
    /**
     * A message indicating a missing value was found. One parameter, a string identifying what the value is, is required.
     */
    public static String emptyValue;

    /**
     * A message indicating a property definition is missing value constraints. One parameter, the property definition name, is
     * required.
     */
    public static String emptyValueConstraints;

    /**
     * A message indicating an invalid value was found. The parameters of property value, property type, and property definition are
     * required.
     */
    public static String errorValidatingPropertyValueForType;

    /**
     * A message indicating the parser could not parse the current toke as it was not a namespace mapping or node type definition.
     * The parameters of parser token value, CND line number, and CND column number are required.
     */
    public static String expectedNamespaceOrNodeDefinition;

    /**
     * A message indicating a <code>find</code> method was called with an invalid value. One parameter, the invalid value, is
     * required.
     */
    public static String invalidFindRequest;

    /**
     * A message indicating a <code>findUsingJcrValue</code> method was called with an invalid value. One parameter, the invalid
     * value, is required.
     */
    public static String invalidFindUsingJcrValueRequest;

    /**
     * A message indicating a <code>getState</code> method was called with an invalid value. One parameter, the invalid value, is
     * required.
     */
    public static String invalidGetStateRequest;

    /**
     * A message indicating a default value of a property definition with a NAME type has an invalid qualifier. Two parameters, the
     * property definition name and the default value, are required.
     */
    public static String invalidQualifierForDefaultValue;

    /**
     * A message indicating an invalid property valid was found. The parameters of invalid property value, property type, and
     * property definition name are required.
     */
    public static String invalidPropertyValueForType;

    /**
     * A message indicating an invalid query operator CND notation was found. Two parameters, the invalid value and the property
     * definition name, are required.
     */
    public static String invalidQueryOperator;

    /**
     * A message indicating an invalid URI was found. One parameter, the invalid URI, is required.
     */
    public static String invalidUri;

    /**
     * An error message indicating that the JSR built-ins CND could not be found in the workspace. One parameter, the path of the
     * CND file, is required.
     */
    public static String jsrBuiltInsCndFileNotFound;

    /**
     * An error message indicating that the JSR built-ins CND could not be found in the filesystem. One parameter, the path of the
     * CND file, is required.
     */
    public static String jsrBuiltInsCndFileNotFoundInFilesystem;

    /**
     * A message indicating a name equal to parent or self was found. One parameter, a string identifying the name, is required.
     */
    public static String localNameEqualToSelfOrParent;

    /**
     * A message indicating a with invalid characters was found. Two parameters, the name of the owner object and a string
     * identifying the type of name, are required.
     */
    public static String localNameHasInvalidCharacters;

    /**
     * A message used for a name when the name is <code>null</code> or empty.
     */
    public static String missingName;

    /**
     * A message indicating multiple default values were found for a single-valued property definition. One parameter, the property
     * definition name, is required.
     */
    public static String multipleDefaultValuesForSingleValuedProperty;

    /**
     * A message indicating the <code>MULTIPLE</code> keyword was found in the CND in a place that is not valid for JCR 2.0. The
     * parameters of CND line number and CND column number are required.
     */
    public static String multipleKeywordNotValidInJcr2CndFormat;

    /**
     * A message indicating the qualifier of a qualified name is not a namespace mapping prefix in the CND. Two parameters, the
     * name/type of the qualified name and the qualifier, are required.
     */
    public static String nameQualifierNotFound;

    /**
     * A message used for the namespace prefix attribute.
     */
    public static String namespacePrefix;

    /**
     * A message used for the namespace URI attribute.
     */
    public static String namespaceUri;

    /**
     * A message indicating a node type definition does not have any property definitions or child node definitions. One parameter,
     * the node type definition name, is required.
     */
    public static String nodeTypeDefinitionHasNoPropertyDefinitionsOrChildNodeDefinitions;

    /**
     * A message used for the node type definition name property.
     */
    public static String nodeTypeDefinitionName;

    /**
     * A message indicating a <code>null</code> was found. One parameter, a string identifying the object, is required.
     */
    public static String objectIsNull;

    /**
     * A message indicating no errors, warnings, or info validation messages were found.
     */
    public static String okValidationMsg;

    /**
     * A message indicating a node type definition has a primary item but has been marked as a variant. One parameter, the node type
     * definition name, is required.
     */
    public static String primaryItemExistsButMarkedAsVariant;

    /**
     * A message used for the primary item name attribute.
     */
    public static String primaryItemName;

    /**
     * A message indicating the <code>PRIMARY</code> keyword was found in the CND in a place that is not valid for JCR 2.0. The
     * parameters of CND line number and CND column number are required.
     */
    public static String primaryKeywordNotValidInJcr2CndFormat;

    /**
     * A message used for the property definition name attribute.
     */
    public static String propertyDefinitionName;

    /**
     * A message indicating a property definition has identified supported query operators but has been marked as a variant. One
     * parameter, the property definition name, is required.
     */
    public static String queryOperatorsExistButMarkedAsVariant;

    /**
     * A message used for the required type name property.
     */
    public static String requiredTypeName;

    /**
     * A message indicating a child node definition has identified supported required types but has been marked as a variant. One
     * parameter, the child node definition name, is required.
     */
    public static String requiredTypesExistButMarkedAsVariant;

    /**
     * A message indicating a <code>null</code> or empty string was found. One parameter, a name identifying the string, is
     * required.
     */
    public static String stringIsEmpty;

    /**
     * A message used for the required type name attribute.
     */
    public static String superTypeName;

    /**
     * A message indicating a node type definition has super types but has been marked as a variant. One parameter, the node type
     * definition name, is required.
     */
    public static String superTypesExistButMarkedAsVariant;

    /**
     * A message indicating a property definition has identified value constraints but has been marked as a variant. One parameter,
     * the property definition name, is required.
     */
    public static String valueConstraintsExistButMarkedAsVariant;

    /**
     * A message indicating a parser error occured processing a vendor block. The parameters or CND line number and CND column are
     * required.
     */
    public static String vendorBlockWasNotClosed;

    static {
        NLS.initializeMessages("org.jboss.tools.modeshape.jcr.messages", Messages.class); //$NON-NLS-1$
    }

    /**
     * Don't allow construction;
     */
    private Messages() {
        // nothing to do
    }
}
