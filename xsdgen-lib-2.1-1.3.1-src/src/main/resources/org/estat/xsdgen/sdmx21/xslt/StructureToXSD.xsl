<?xml version="1.0" encoding="UTF-8"?>

<!--

Version 1.0.0 - Wednesday, May 25, 2011
Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.

Notes:

1. Requires a XSLT version 2.0+ processor.
2. Tested using Saxon-HE 9.3.0 (http://saxon.sourceforge.net).
3. Does not support external structure:DataStructure elements. Support for external codelists or concepts is
implemented, so should be easy to use same mechanism for DSD too...

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:message="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message"
                xmlns:common="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common"
                xmlns:structure="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/structure"
                xmlns:dsd="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/structurespecific"
                xmlns:func="www.sdmx.org/resources/sdmxml/schemas/v2_1/functions"
                version="2.0">

    <!-- Output settings. -->
    <xsl:output method="html" encoding="UTF-8" indent="yes" media-type="text/xml"/>


    <!-- PARAMETERS -->
    <!-- ============================== -->


    <!-- Specifies if the resulted XSD is for time series messages. -->
    <xsl:param name="Param_IsTimeSeries" as="xs:boolean">false</xsl:param>
    <!-- The dimension at the observation level. If is 'AllDimensions' then the resulted XSD will be for flat
    messages. This is ignored if Param_IsTimeSeries is TRUE because only the time dimension can be
    at observation level. -->
    <xsl:param name="Param_DimensionAtObservation" as="xs:string">TIME_PERIOD</xsl:param>
    <!-- Indicates whether explicit measures are used in the cross sectional format. This is used only
    if the measure dimension is at observation level. -->
    <xsl:param name="Param_ExplicitMeasures" as="xs:boolean">true</xsl:param>
    <!-- Optional parameters required when the source file contains multiple data structure definitions. They are
    not mandatory because there should be only one DSD defined into source file. -->
    <xsl:param name="Param_StructureId" as="xs:string" select="''"/>
    <xsl:param name="Param_StructureAgencyID" as="xs:string" select="''"/>
    <xsl:param name="Param_StructureVersion" as="xs:string" select="''"/>
    <!-- Optional parameter specifying the namespace to be used for resulted XSD. If is empty then a default
    one will be generated using a standard convention. -->
    <xsl:param name="Param_Namespace" as="xs:string" select="''"/>
    <!-- Optional parameter specifying a prefix to be used for the names of SDMX schemas imported by
    resulted XSD. This can be used to specify the base path for example (and in this case must
    end with '/' character). -->
    <xsl:param name="Param_ImportedSchemaPrefix" as="xs:string" select="''"/>


    <!-- VARIABLES -->
    <!-- ============================== -->


    <!-- A RTF with all required items which may be defined externally (codelists and concept schemes). -->
    <xsl:variable name="Var_ItemsRTF">
        <xsl:for-each select="/message:Structure/message:Structures/structure:*[local-name()='Codelists' or local-name()='Concepts']/structure:*">
            <xsl:copy-of select="func:GetFullItem(.)"/>
        </xsl:for-each>
    </xsl:variable>


    <!-- The DSD element. -->
    <!-- TODO: to add support for DSDs defined externally -->
    <xsl:variable name="Var_StructureElm" select="/message:Structure/message:Structures/structure:DataStructures/structure:DataStructure[$Param_StructureId='' or @id=$Param_StructureId][$Param_StructureAgencyID='' or @agencyID=$Param_StructureAgencyID][$Param_StructureVersion='' or @version=$Param_StructureVersion]"/>
    <!-- The structure:DimensionList element. -->
    <xsl:variable name="Var_DimensionListElm" select="$Var_StructureElm/structure:DataStructureComponents/structure:DimensionList"/>
    <!-- The structure:AttributeList element. -->
    <xsl:variable name="Var_AttributeListElm" select="$Var_StructureElm/structure:DataStructureComponents/structure:AttributeList"/>
    <!-- The optional time dimension element. Note that according to SDMX 2.1 XSDs there can be multiple time dimensions :) -->
    <xsl:variable name="Var_TimeDimensionElm" select="$Var_DimensionListElm/structure:TimeDimension"/>
    <!-- The primary measure element. -->
    <xsl:variable name="Var_PrimaryMeasureElm" select="$Var_StructureElm/structure:DataStructureComponents/structure:MeasureList/structure:PrimaryMeasure"/>
    <!-- True if the DSD has groups, false otherwise. -->
    <xsl:variable name="Var_HasGroups" as="xs:boolean" select="count($Var_StructureElm/structure:DataStructureComponents/structure:Group)>0"/>


    <!-- The dimension at the observation level. This is set to Param_DimensionAtObservation for non time series XSDs,
    otherwise is forced to time dimension (and Param_DimensionAtObservation is simply ignored). -->
    <xsl:variable name="Var_DimensionAtObservation" as="xs:string">
        <xsl:choose>
            <!-- For time series messages the time dimension is always at observation level. -->
            <xsl:when test="$Param_IsTimeSeries">
                <xsl:choose>
                    <xsl:when test="empty($Var_TimeDimensionElm)">
                        <xsl:message terminate="yes">Cannot create an XSD for time series messages because the DSD does not define a time dimension.</xsl:message>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="func:GetComponentId($Var_TimeDimensionElm)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <!-- For other messages the dimension at observation level can be 'AllDimensions', a dimension, a measure dimension or even the time dimension. -->
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="$Param_DimensionAtObservation='AllDimensions'">AllDimensions</xsl:when>
                    <xsl:when test="count($Var_DimensionListElm/structure:*[func:GetComponentId(.)=$Param_DimensionAtObservation])>0"><xsl:value-of select="$Param_DimensionAtObservation"/></xsl:when>
                    <xsl:otherwise>
                        <xsl:message terminate="yes">Invalid value for Param_DimensionAtObservation parameter: <xsl:value-of select="$Param_DimensionAtObservation"/></xsl:message>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>


    <!-- True if the resulted XSD must be for flat messages, false otherwise. -->
    <xsl:variable name="Var_IsFlat" as="xs:boolean" select="$Var_DimensionAtObservation='AllDimensions'"/>


    <!-- The explicit measure dimension. This is set only if the measure dimension is at observation level and only
    if Param_ExplicitMeasures is TRUE, otherwise is an empty string. -->
    <xsl:variable name="Var_ExplicitMeasureDimension" as="xs:string">
        <xsl:variable name="measureDimElm" select="$Var_DimensionListElm/structure:MeasureDimension"/>
        <xsl:choose>
            <xsl:when test="$Param_ExplicitMeasures and not(empty($measureDimElm)) and ($Var_IsFlat or (func:GetComponentId($measureDimElm)=$Var_DimensionAtObservation))">
                <xsl:value-of select="func:GetComponentId($measureDimElm)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="''"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>


    <!-- The target namespace used for resulted XSD file. -->
    <!-- urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=${agencyID}:${id}(${version}):ObsLevelDim:${dimension} -->
    <xsl:variable name="Var_Namespace" as="xs:string">
        <xsl:choose>
            <xsl:when test="$Param_Namespace=''">
                <xsl:value-of>
                    <xsl:text>urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=</xsl:text>
                    <xsl:value-of select="concat($Var_StructureElm/@agencyID, ':', $Var_StructureElm/@id)"/>
                    <xsl:if test="not(empty($Var_StructureElm/@version))">
                        <xsl:value-of select="concat('(', $Var_StructureElm/@version, ')')"/>
                    </xsl:if>
                    <xsl:text>:ObsLevelDim:</xsl:text>
                    <xsl:value-of select="$Var_DimensionAtObservation"/>
                    <xsl:if test="$Var_ExplicitMeasureDimension!=''">
                        <xsl:text>:explicit</xsl:text>
                    </xsl:if>
                </xsl:value-of>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$Param_Namespace"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>


    <!-- FUNCTIONS -->
    <!-- ============================== -->


    <!-- Creates a string representation for an item identified using the ID, the agency ID and optionally the
    version. This is used only for error messages, to provide more details when something goes wrong. -->
    <xsl:function name="func:ItemToString" as="xs:string">
        <xsl:param name="itemElm"/>

        <xsl:variable name="tmp">
            <xsl:value-of select="concat('id=', $itemElm/@id, ' agencyID=', $itemElm/@agencyID)"/>
            <xsl:if test="not(empty($itemElm/@version))">
                <xsl:value-of select="concat(' version=', $itemElm/@version)"/>
            </xsl:if>
        </xsl:variable>

        <xsl:value-of select="string($tmp)"/>
    </xsl:function>


    <!-- Returns a full item. -->
    <!-- This is required for codelists and concept schemes, which may be defined externally. -->
    <xsl:function name="func:GetFullItem">
        <!-- The item, which may be full or it may be defined externally. -->
        <xsl:param name="itemElm"/>

        <xsl:variable name="itemRTF">
            <xsl:choose>
                <xsl:when test="$itemElm/@isExternalReference=true()">
                    <xsl:variable name="url" select="if (empty($itemElm/@structureURL)) then $itemElm/@serviceURL else $itemElm/@structureURL"/>
                    <xsl:if test="empty($url)">
                        <xsl:message terminate="yes">
                            <xsl:text>ERROR: missing source URL for externally defined item </xsl:text>
                            <xsl:value-of select="func:ItemToString($itemElm)"/>
                        </xsl:message>
                    </xsl:if>
                    <xsl:variable name="externItemElm" select="document($url, root($itemElm))/message:Structure/message:Structures/*/*[(@id=$itemElm/@id) and (@agencyID=$itemElm/@agencyID) and (empty($itemElm/@version) or (@version=$itemElm/@version))]"/>
                    <xsl:choose>
                        <xsl:when test="count($externItemElm)!=1">
                            <xsl:message terminate="yes">
                                <xsl:text>ERROR: failed to find exactly one </xsl:text>
                                <xsl:value-of select="func:ItemToString($itemElm)"/>
                                <xsl:text> item inside document retrieved from URL </xsl:text>
                                <xsl:value-of select="$url"/>
                            </xsl:message>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:copy-of select="func:GetFullItem($externItemElm)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$itemElm"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:copy-of select="$itemRTF/*"/>
    </xsl:function>


    <!-- Finds and returns a referenced concept or codelist item. -->
    <xsl:function name="func:GetReferencedItemElm">
        <!-- A common:ConceptReferenceType or a common:CodelistReferenceType element. -->
        <xsl:param name="refElm"/>

        <xsl:choose>
            <!-- the reference element has a Ref child -->
            <xsl:when test="not(empty($refElm/Ref))">
                <xsl:variable name="elm" select="$refElm/Ref"/>
                <xsl:variable name="itemRTF">
                    <xsl:choose>
                        <!-- a common:CodelistReferenceType -->
                        <xsl:when test="empty($elm/@maintainableParentID)">
                            <xsl:copy-of select="$Var_ItemsRTF//*[@id=$elm/@id and @agencyID=$elm/@agencyID and (empty($elm/@version) or (@version=$elm/@version))]"/>
                        </xsl:when>
                        <!-- this is a common:ConceptReferenceType -->
                        <xsl:otherwise>
                            <xsl:copy-of select="$Var_ItemsRTF//*[@id=$elm/@maintainableParentID and @agencyID=$elm/@agencyID and (empty($elm/@version) or (@version=$elm/@version))]/structure:Concept[@id=$elm/@id]"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="count($itemRTF/*)!=1">
                        <xsl:message terminate="yes">ERROR: Failed to find exactly one item identified using <xsl:copy-of select="$elm"/></xsl:message>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy-of select="$itemRTF/*"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <!-- the reference element must have a URN child -->
            <xsl:otherwise>
                <xsl:variable name="urn" as="xs:string" select="normalize-space($refElm/URN/text())"/>
                <xsl:variable name="itemElm" select="$Var_ItemsRTF//*[@urn=$urn]"/>
                <xsl:choose>
                    <xsl:when test="count($itemElm)!=1">
                        <xsl:message terminate="yes">ERROR: Failed to find exactly one item identified using URN <xsl:value-of select="$urn"/></xsl:message>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy-of select="$itemElm"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>


    <!-- Getter for the ID of a DSD component. -->
    <!-- According to SDMX 2.1, the id attribute holds an explicit identification of the component. If this identifier
    is not supplied then it is assumed to be the same as the identifier of the concept referenced from the
    concept identity. -->
    <xsl:function name="func:GetComponentId">
        <!-- Component's element, included into DSD. -->
        <xsl:param name="componentElm"/>

        <xsl:choose>
            <xsl:when test="empty($componentElm/@id)">
                <!-- TODO: this can be optimized if the concept is not referenced using the URN -->
                <xsl:variable name="conceptElm" select="func:GetReferencedItemElm($componentElm/structure:ConceptIdentity)"/>
                <xsl:value-of select="$conceptElm/@id"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$componentElm/@id"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>


    <!-- Returns the element with the representation data for a DSD component: the structure:LocalRepresentation
    if any or concept's structure:CoreRepresentation. -->
    <xsl:function name="func:GetComponentRepresentationElm">
        <!-- The component element (a dimension or an attribute). -->
        <xsl:param name="componentElm"/>

        <xsl:variable name="rtf">
            <xsl:variable name="localRepElm" select="$componentElm/structure:LocalRepresentation"/>
            <xsl:choose>
                <xsl:when test="empty($localRepElm)">
                    <xsl:variable name="conceptElm" select="func:GetReferencedItemElm($componentElm/structure:ConceptIdentity)"/>
                    <xsl:variable name="coreRepElm" select="$conceptElm/structure:CoreRepresentation"/>
                    <xsl:choose>
                        <xsl:when test="empty($coreRepElm)">
                            <xsl:message terminate="yes">ERROR: The concept <xsl:value-of select="$conceptElm/@id"/> does not have a structure:CoreRepresentation element. Note that ISOConceptReference is not supported.</xsl:message>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:copy-of select="$coreRepElm"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$localRepElm"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:copy-of select="$rtf/*"/>
    </xsl:function>


    <!-- Returns the local name (without namespace prefix) of the XSD type coresponding to a concept scheme or codelist. -->
    <xsl:function name="func:GetLocalTypeNameForItem" as="xs:string">
        <!-- The concept scheme or the codelist. -->
        <xsl:param name="itemElm"/>
        <xsl:value-of select="concat($itemElm/@agencyID, '.', $itemElm/@id, if ($itemElm/@version!='') then concat('.', $itemElm/@version) else '')"/>
    </xsl:function>


    <!-- Returns the full name (including namespace prefix) of the XSD type for a DSD component. -->
    <xsl:function name="func:GetComponentTypeName">
        <!-- The DSD component (a dimension or an attribute). -->
        <xsl:param name="componentElm"/>

        <xsl:variable name="repElm" select="func:GetComponentRepresentationElm($componentElm)"/>

        <xsl:choose>
            <xsl:when test="count($repElm/structure:Enumeration)=1">
                <xsl:variable name="codelistElm" select="func:GetReferencedItemElm($repElm/structure:Enumeration)"/>
                <xsl:value-of select="'ns:'"/>
                <xsl:value-of select="func:GetLocalTypeNameForItem($codelistElm)"/>
            </xsl:when>
            <xsl:otherwise>
                <!-- See common:BasicComponentDataType for all possible types; see common:DataType for some hints. -->
                <!-- TODO: to add missing types -->
                <xsl:variable name="fmt" select="$repElm/structure:TextFormat/@textType"/>
                <xsl:choose>
                    <xsl:when test="$fmt='Alpha'">common:AlphaType</xsl:when>
                    <xsl:when test="$fmt='AlphaNumeric'">common:AlphaNumericType</xsl:when>
                    <xsl:when test="$fmt='Numeric'">common:NumericType</xsl:when>
                    <xsl:when test="$fmt='BigInteger'">xs:integer</xsl:when>
                    <xsl:when test="$fmt='Integer'">xs:int</xsl:when>
                    <xsl:when test="$fmt='Long'">xs:long</xsl:when>
                    <xsl:when test="$fmt='Short'">xs:short</xsl:when>
                    <xsl:when test="$fmt='Decimal'">xs:decimal</xsl:when>
                    <xsl:when test="$fmt='Float'">xs:float</xsl:when>
                    <xsl:when test="$fmt='Double'">xs:double</xsl:when>
                    <xsl:when test="$fmt='Boolean'">xs:boolean</xsl:when>
                    <xsl:when test="$fmt='URI'">xs:anyURI</xsl:when>

                    <xsl:when test="$fmt='ObservationalTimePeriod'">common:ObservationalTimePeriodType</xsl:when>
                    <xsl:when test="$fmt='StandardTimePeriod'">common:StandardTimePeriodType</xsl:when>
                    <xsl:when test="$fmt='BasicTimePeriod'">common:BasicTimePeriodType</xsl:when>
                    <xsl:when test="$fmt='GregorianTimePeriod'">common:GregorianTimePeriodType</xsl:when>

                    <xsl:otherwise>xs:string</xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>


    <!-- Returns TRUE if an attribute can be attached to a specified level, FALSE otherwise. -->
    <!-- TODO: fix this -->
    <xsl:function name="func:AttributeCanBeAttachedToLevel" as="xs:boolean">
        <!-- the structure:Attribute element -->
        <xsl:param name="attributeElm"/>
        <!-- the level as a string: dataset, group, series, observation -->
        <xsl:param name="level" as="xs:string"/>
        <!-- the ID of the group, required only if $level='group'; empty string or '*' means ALL groups -->
        <xsl:param name="groupId" as="xs:string"/>

        <xsl:variable name="relElm" select="$attributeElm/structure:AttributeRelationship"/>

        <xsl:choose>
            <!-- This means that value of the attribute will not vary with any of the other key
            family components. This will always be treated as a data set level attribute. -->
            <xsl:when test="not(empty($relElm/structure:None))">
                <xsl:value-of select="$level='dataset'"/>
            </xsl:when>

            <!-- This is used to specify that the value of the attribute is dependent upon the
            observed value. An attribute with this relationship will always be treated as
            an observation level attribute. -->
            <xsl:when test="count($relElm/structure:PrimaryMeasure)>0">
                <xsl:value-of select="$level='observation'"/>
            </xsl:when>

            <!-- This is used to specify that the attribute should always be attached to the
            referenced groups. Note that if one of the referenced dimensions is the time
            dimension, the groups referenced here will be ignored. -->
            <xsl:when test="count($relElm/structure:AttachmentGroup)>0">
                <xsl:choose>
                    <xsl:when test="$level!='group'">
                        <xsl:value-of select="false()"/>
                    </xsl:when>
                    <xsl:when test="$groupId='' or $groupId='*'">
                        <xsl:value-of select="true()"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="count($relElm/structure:AttachmentGroup/Ref[@id=$groupId])>0"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>

            <!-- This is used as a convenience to referencing all of the dimension defined by
            the referenced group. The attribute will also be attached to this group.-->
            <xsl:when test="count($relElm/structure:Group)>0">
                <xsl:choose>
                    <xsl:when test="$level!='group'">
                        <xsl:value-of select="false()"/>
                    </xsl:when>
                    <xsl:when test="$groupId='' or $groupId='*'">
                        <xsl:value-of select="true()"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="count($relElm/structure:Group/Ref[@id=$groupId])>0"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>

            <!-- This is used to reference dimensions in the data structure definition on
            which the value of this attribute depends. An attribute using this relationship can
            be either a group, series (or section), or observation level attribute. The
            attachment level of the attribute will be determined by the data format and which
            dimensions are referenced. -->
            <xsl:when test="count($relElm/structure:Dimension)>0">
                <xsl:choose>
                    <xsl:when test="count($relElm/structure:Dimension/Ref[@id=$Var_DimensionAtObservation])=0">
                        <xsl:copy-of select="$level='series'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy-of select="$level='observation'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>

            <!-- Something is wrong. -->
            <xsl:otherwise>
                <xsl:message terminate="no">WARNING: failed to get the attachment level for attribute <xsl:value-of select="func:GetComponentId($attributeElm)"/></xsl:message>
                <xsl:copy-of select="false()"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>


    <!-- TEMPLATES -->
    <!-- ============================== -->


    <!-- This is the main template, the only one with a "match" attribute. -->
    <xsl:template match="/">
        <!-- Some basic validation, to be sure that there's one and only one DSD element. -->
        <xsl:choose>
            <xsl:when test="count($Var_StructureElm)=0">
                <xsl:message terminate="yes">Failed to retrieve the structure:DataStructure element.</xsl:message>
            </xsl:when>
            <xsl:when test="count($Var_StructureElm)>1">
                <xsl:message terminate="yes">Found multiple structure:DataStructure elements. Specify the ID, agency's ID and the version.</xsl:message>
            </xsl:when>
        </xsl:choose>

        <!-- The resulted XSD document. -->
        <xs:schema targetNamespace="{$Var_Namespace}" elementFormDefault="qualified" attributeFormDefault="unqualified">
            <xsl:namespace name="ns" select="$Var_Namespace"/>

            <xs:import namespace="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message" schemaLocation="{$Param_ImportedSchemaPrefix}SDMXMessage.xsd"/>
            <xs:import namespace="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/common" schemaLocation="{$Param_ImportedSchemaPrefix}SDMXCommon.xsd"/>
            <xs:import namespace="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/data/structurespecific" schemaLocation="{$Param_ImportedSchemaPrefix}SDMXDataStructureSpecific.xsd"/>

            <xsl:call-template name="Template_DataSetType"/>
            <xsl:if test="$Var_HasGroups">
                <xsl:call-template name="Template_GroupsType"/>
            </xsl:if>
            <xsl:if test="not($Var_IsFlat)">
                <xsl:call-template name="Template_SeriesType"/>
            </xsl:if>
            <xsl:call-template name="Template_ObsType"/>
            <xsl:if test="$Var_ExplicitMeasureDimension!=''">
                <xsl:call-template name="Template_ExplicitMeasureTypes"/>
            </xsl:if>
            <xsl:call-template name="Template_ItemTypes"/>
        </xs:schema>
    </xsl:template>


    <!-- Outputs the XSD type for DataSet element. -->
    <xsl:template name="Template_DataSetType">
        <xs:complexType name="DataSetType">
            <xs:complexContent>
                <xs:restriction base="{if ($Param_IsTimeSeries) then 'dsd:TimeSeriesDataSetType' else 'dsd:DataSetType'}">
                    <xs:sequence>
                        <!-- these are from base type -->
                        <xs:element ref="common:Annotations" minOccurs="0"/>
                        <xs:element name="DataProvider" type="common:DataProviderReferenceType" form="unqualified" minOccurs="0"/>
                        <!-- the groups (if any) -->
                        <xsl:if test="$Var_HasGroups">
                            <xs:element name="Group" type="ns:GroupType" form="unqualified" minOccurs="0" maxOccurs="unbounded"/>
                        </xsl:if>
                        <!-- the observations (only for flat messages) or the series -->
                        <xs:choice minOccurs="0">
                            <xsl:choose>
                                <xsl:when test="$Var_IsFlat">
                                    <xs:element name="Obs" type="ns:ObsType" form="unqualified" maxOccurs="unbounded"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xs:element name="Series" type="ns:SeriesType" form="unqualified" maxOccurs="unbounded"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xs:choice>
                    </xs:sequence>
                    <!--the attributes-->
                    <!-- TODO: this was not tested (no samples available) -->
                    <xsl:for-each select="$Var_AttributeListElm/structure:Attribute">
                        <xsl:if test="func:AttributeCanBeAttachedToLevel(., 'dataset', '')">
                            <!-- they are always optional? there's nothing in DataSetType's description about this -->
                            <xs:attribute name="{func:GetComponentId(.)}" type="{func:GetComponentTypeName(.)}" use="optional"/>
                        </xsl:if>
                    </xsl:for-each>
                    <!-- TODO: this is hardcoded and must be fixed -->
                    <xs:attribute name="REPORTING_YEAR_START_DAY" type="xs:gMonthDay" use="prohibited"/>
                </xs:restriction>
            </xs:complexContent>
        </xs:complexType>
    </xsl:template>


    <!-- Outputs the XSD type for Group elements. -->
    <xsl:template name="Template_GroupsType">
        <!-- First outputs an enumeration with all group IDs. -->
        <xs:simpleType name="GroupType.ID">
            <xs:restriction base="common:IDType">
                <xsl:for-each select="$Var_StructureElm/structure:DataStructureComponents/structure:Group">
                    <xs:enumeration value="{@id}"/>
                </xsl:for-each>
            </xs:restriction>
        </xs:simpleType>

        <!-- The base type for all groups. This is required because is referenced from DataSetType element. -->
        <xs:complexType name="GroupType" abstract="true">
            <xs:complexContent>
                <xs:restriction base="dsd:GroupType">
                    <xs:attribute name="type" type="ns:GroupType.ID" use="optional"/>
                    <xs:anyAttribute namespace="##local"/>
                </xs:restriction>
            </xs:complexContent>
        </xs:complexType>


        <!-- A type for each group. -->
        <xsl:for-each select="$Var_StructureElm/structure:DataStructureComponents/structure:Group">
            <xsl:variable name="gid" select="@id"/>
            <xs:complexType name="{$gid}">
		        <xs:complexContent>
			        <xs:restriction base="ns:GroupType">
                        <!-- all dimensions referenced by group -->
                        <xsl:for-each select="structure:GroupDimension/structure:DimensionReference/Ref">
                            <xsl:variable name="dimRefId" select="@id"/>
                            <xsl:variable name="dimElm" select="$Var_DimensionListElm/structure:*[func:GetComponentId(.)=$dimRefId]"/>
                            <xs:attribute name="{$dimRefId}" type="{func:GetComponentTypeName($dimElm)}" use="required"/>
                        </xsl:for-each>
                        <!-- group's attributes; the documentation for GroupType specifies that they are always optional -->
                        <xsl:for-each select="$Var_AttributeListElm/structure:Attribute">
                            <xsl:if test="func:AttributeCanBeAttachedToLevel(., 'group', $gid)">
                                <xs:attribute name="{func:GetComponentId(.)}" type="{func:GetComponentTypeName(.)}" use="optional"/>
                            </xsl:if>
                        </xsl:for-each>
                        <!-- additional attributes. -->
                        <xs:attribute name="type" type="ns:GroupType.ID" use="optional" fixed="{$gid}"/>
                        <!-- TODO: this is hardcoded and must be fixed -->
                        <xs:attribute name="REPORTING_YEAR_START_DAY" type="xs:gMonthDay" use="prohibited"/>
			        </xs:restriction>
		        </xs:complexContent>
	        </xs:complexType>
        </xsl:for-each>
    </xsl:template>


    <!-- Outputs the XSD type for series. Must be used only for non-flat messages. -->
    <xsl:template name="Template_SeriesType">
        <xs:complexType name="SeriesType">
            <xs:complexContent>
                <xs:restriction base="{if ($Param_IsTimeSeries) then 'dsd:TimeSeriesType' else 'dsd:SeriesType'}">
                    <!-- these are from base type -->
                    <!-- TODO: to see if is required to output this -->
                    <xs:sequence>
                        <xs:element ref="common:Annotations" minOccurs="0"/>
                        <xs:element name="Obs" type="ns:ObsType" form="unqualified" minOccurs="0" maxOccurs="unbounded"/>
                    </xs:sequence>
                    <!-- the dimensions (except the one declared to be at observation level) -->
                    <xsl:for-each select="$Var_DimensionListElm/*">
                        <xsl:variable name="did" select="func:GetComponentId(.)"/>
                        <xsl:if test="$did!=$Var_DimensionAtObservation">
                            <xs:attribute name="{$did}" type="{func:GetComponentTypeName(.)}" use="required"/>
                        </xsl:if>
                    </xsl:for-each>
                    <!-- the attributes; the documentation for SeriesType specifies that they are always optional  -->
                    <xsl:for-each select="$Var_AttributeListElm/structure:Attribute">
                        <xsl:if test="func:AttributeCanBeAttachedToLevel(., 'series', '')">
                            <xs:attribute name="{func:GetComponentId(.)}" type="{func:GetComponentTypeName(.)}" use="optional"/>
                        </xsl:if>
                    </xsl:for-each>
                    <!-- TODO: this is hardcoded and must be fixed -->
                    <xs:attribute name="REPORTING_YEAR_START_DAY" type="xs:gMonthDay" use="prohibited"/>
                </xs:restriction>
            </xs:complexContent>
        </xs:complexType>
    </xsl:template>

    <!-- Outputs the XSD type for observations. -->
    <xsl:template name="Template_ObsType">
        <xs:complexType name="ObsType">
            <xs:complexContent>
                <xs:restriction base="{if ($Param_IsTimeSeries) then 'dsd:TimeSeriesObsType' else 'dsd:ObsType'}">
                    <!-- the dimensions, except time and explicit measure -->
                    <xsl:choose>
                        <xsl:when test="$Var_IsFlat">
                            <!-- for flat messages all dimensions are at observation level -->
                            <xsl:for-each select="$Var_DimensionListElm/*">
                                <xsl:variable name="did" select="func:GetComponentId(.)"/>
                                <xsl:if test="$did!='TIME_PERIOD' and $did!=$Var_ExplicitMeasureDimension">
                                    <xs:attribute name="{$did}" type="{func:GetComponentTypeName(.)}" use="required"/>
                                </xsl:if>
                            </xsl:for-each>
                        </xsl:when>
                        <xsl:otherwise>
                            <!-- for non flat messages outputs the dimension at observation level, except the time
                            dimension (this is handled later) and explicit measure dimension -->
                            <xsl:if test="$Var_DimensionAtObservation!='TIME_PERIOD' and $Var_ExplicitMeasureDimension=''">
                                <xsl:variable name="dimElm" select="$Var_DimensionListElm/structure:*[func:GetComponentId(.)=$Var_DimensionAtObservation]"/>
                                <xs:attribute name="{$Var_DimensionAtObservation}" type="{func:GetComponentTypeName($dimElm)}" use="required"/>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                    <!-- the time dimension (if any) is always required, except for XS messages with another dimension at observation level -->
                    <xsl:if test="not(empty($Var_TimeDimensionElm))">
                        <xsl:variable name="status" select="if (not($Var_IsFlat) and ($Var_DimensionAtObservation!='TIME_PERIOD')) then 'prohibited' else 'required'"/>
                        <xs:attribute name="TIME_PERIOD" type="common:ObservationalTimePeriodType" use="{$status}"/>
                    </xsl:if>
                    <!-- the primary measure dimension; this is really optional? -->
                    <xs:attribute name="OBS_VALUE" type="{func:GetComponentTypeName($Var_PrimaryMeasureElm)}" use="optional"/>
                    <!-- the attributes -->
                    <xsl:for-each select="$Var_AttributeListElm/structure:Attribute">
                        <xsl:if test="$Var_IsFlat or func:AttributeCanBeAttachedToLevel(., 'observation', '')">
                            <!-- TODO: to see if they are really optional -->
                            <!-- {if (@assignmentStatus='Mandatory') then 'required' else 'optional'} -->
                            <xs:attribute name="{func:GetComponentId(.)}" type="{func:GetComponentTypeName(.)}" use="optional"/>
                        </xsl:if>
                    </xsl:for-each>
                    <!-- the type attribute -->
                    <xsl:choose>
                        <xsl:when test="$Var_ExplicitMeasureDimension!=''">
                            <!-- TODO: to see if this is really optional (see ObsType description: "this will not be required") -->
                            <xs:attribute name="type" type="{func:GetComponentTypeName($Var_DimensionListElm/structure:MeasureDimension)}" use="optional"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xs:attribute name="type" type="common:IDType" use="prohibited"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    <!-- TODO: this is hardcoded and must be fixed -->
                    <xs:attribute name="REPORTING_YEAR_START_DAY" type="xs:gMonthDay" use="prohibited"/>
                </xs:restriction>
            </xs:complexContent>
        </xs:complexType>
    </xsl:template>


    <!-- Outputs the XSD types for the concepts included into the concept scheme associated with the
    explicit measure dimension. According to SDMX v2.1 (see MeasureDimension element) the measure dimension always
    takes it representation from a concept scheme, which defines the measures and their representations. -->
    <xsl:template name="Template_ExplicitMeasureTypes">
        <xsl:variable name="measureDimElm" select="$Var_DimensionListElm/structure:MeasureDimension"/>
        <xsl:variable name="repElm" select="func:GetComponentRepresentationElm($measureDimElm)"/>
        <xsl:if test="count($repElm/structure:Enumeration)!=1">
            <xsl:message terminate="yes">ERROR: The representation element for measure dimension does not have exactly one enumeration.</xsl:message>
        </xsl:if>
        <xsl:variable name="conceptSchemeElm" select="func:GetReferencedItemElm($repElm/structure:Enumeration)"/>
        <xsl:variable name="typeName" select="func:GetComponentTypeName($measureDimElm)"/>
        <xsl:for-each select="$conceptSchemeElm/structure:Concept">
            <xs:complexType name="{@id}">
                <xs:complexContent>
                    <xs:restriction base="ns:ObsType">
                        <xs:attribute name="type" type="{$typeName}" use="optional" fixed="{@id}"/>
                        <!-- TODO: to add OBS_VALUE element -->
                    </xs:restriction>
                </xs:complexContent>
            </xs:complexType>
        </xsl:for-each>
    </xsl:template>


    <!-- Outputs the XSD types for used concept schemes and codelists. -->
    <xsl:template name="Template_ItemTypes">
        <!-- First creates a RTF with all item types. This is required because there can be multiple components
        for which same XSD type is generated. -->
        <xsl:variable name="rtf">
            <xsl:for-each select="$Var_StructureElm/structure:DataStructureComponents/*/*[local-name()='Dimension' or local-name()='MeasureDimension' or local-name()='Attribute']">
                <xsl:variable name="repElm" select="func:GetComponentRepresentationElm(.)"/>
                <xsl:if test="count($repElm/structure:Enumeration)=1">
                    <xsl:variable name="itemElm" select="func:GetReferencedItemElm($repElm/structure:Enumeration)"/>
                    <xs:simpleType name="{func:GetLocalTypeNameForItem($itemElm)}">
                        <xs:restriction base="common:IDType">
                            <xsl:choose>
                                <!-- codelist -->
                                <xsl:when test="local-name($itemElm)='Codelist'">
                                    <xsl:for-each select="$itemElm/structure:Code">
                                        <xs:enumeration value="{@id}"/>
                                    </xsl:for-each>
                                </xsl:when>
                                <!-- concept scheme -->
                                <xsl:otherwise>
                                    <xsl:for-each select="$itemElm/structure:Concept">
                                        <xs:enumeration value="{@id}"/>
                                    </xsl:for-each>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xs:restriction>
                    </xs:simpleType>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>

        <!-- Copies to output the resulted types, but without any duplicates. -->
        <xsl:for-each select="distinct-values($rtf/*/@name)">
            <xsl:variable name="name" select="."/>
            <xsl:copy-of select="$rtf/*[@name=$name][1]"/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
