<?xml version="1.0" encoding="UTF-8"?>
<!-- Updated on /07/2009 to support uncoded Dimensions & PrimaryMeasure. Shipped with Eurostat's SDMX Registry v3.3.16 -->
<xsl:stylesheet
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:common="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/common"
xmlns:message="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message"
xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
xmlns:cross="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/cross"
xmlns:xs="http://www.w3.org/2001/XMLSchema"
exclude-result-prefixes="structure message"
version="2.0">
     <xsl:output method="xml" indent="yes"/>
     <xsl:param name="Namespace">nameSpaceHolder</xsl:param>
     <xsl:param name="CommonURI">SDMXCommon.xsd</xsl:param>
     <xsl:param name="CrossURI">SDMXCrossSectionalData.xsd</xsl:param>
     <xsl:param name="KeyFamily">1</xsl:param>
     <xsl:template match="/">
         
          <xs:schema>
               <xsl:namespace name=""><xsl:value-of select="//structure:KeyFamily/@urn"/><xsl:value-of select="$Namespace"/></xsl:namespace>
               <xsl:attribute name="targetNamespace"><xsl:value-of select="//structure:KeyFamily/@urn"/><xsl:value-of select="$Namespace"/></xsl:attribute>              
               <xsl:attribute name="elementFormDefault"><xsl:text>qualified</xsl:text></xsl:attribute>
               <xsl:attribute name="attributeFormDefault"><xsl:text>unqualified</xsl:text></xsl:attribute>
               <xsl:element name="xs:import">
                    <xsl:attribute name="namespace"><xsl:text>http://www.SDMX.org/resources/SDMXML/schemas/v2_0/cross</xsl:text></xsl:attribute>
                    <xsl:attribute name="schemaLocation"><xsl:value-of select="$CrossURI"/></xsl:attribute>
               </xsl:element>
               <xsl:element name="xs:import">
                    <xsl:attribute name="namespace"><xsl:text>http://www.SDMX.org/resources/SDMXML/schemas/v2_0/common</xsl:text></xsl:attribute>
                    <xsl:attribute name="schemaLocation"><xsl:value-of select="$CommonURI"/></xsl:attribute>
               </xsl:element>
               <xsl:element name="xs:element">
                    <xsl:attribute name="name"><xsl:text>DataSet</xsl:text></xsl:attribute>
                    <xsl:attribute name="type"><xsl:text>DataSetType</xsl:text></xsl:attribute>
                    <xsl:attribute name="substitutionGroup"><xsl:text>cross:DataSet</xsl:text></xsl:attribute>
               </xsl:element>
               <!-- Data Set Complex Type-->
               <xsl:element name="xs:complexType">
                    <xsl:attribute name="name"><xsl:text>DataSetType</xsl:text></xsl:attribute>
                    <xsl:element name="xs:complexContent">
                         <xsl:element name="xs:extension">
                              <xsl:attribute name="base"><xsl:text>cross:DataSetType</xsl:text></xsl:attribute>
                              <xsl:element name="xs:choice">
                                   <xsl:attribute name="minOccurs"><xsl:text>0</xsl:text></xsl:attribute>
                                   <xsl:attribute name="maxOccurs"><xsl:text>unbounded</xsl:text></xsl:attribute>
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="ref"><xsl:text>Group</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Group)-->
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="ref"><xsl:text>Section</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Section)-->
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="name"><xsl:text>Annotations</xsl:text></xsl:attribute>
                                        <xsl:attribute name="type"><xsl:text>common:AnnotationsType</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Annotations)-->
                              </xsl:element>
                              <!--Close xs:choice-->
                              <xsl:for-each select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/*[@crossSectionalAttachDataSet='true']">
                                   <xsl:element name="xs:attribute">
                                        <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:if test="@codelist">
                                                  <xsl:value-of select="@codelist"/>
                                             </xsl:if>
                                             <xsl:if test="not(@codelist)">
                                                  <xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
                                        <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:attribute (of DataSetType)-->
                              </xsl:for-each>
                         </xsl:element>
                         <!--Close xs:extension-->
                    </xsl:element>
                    <!--Close xs:complexContent-->
               </xsl:element>
               <!--Close xs:complexType-->
               <!-- End Data Set Complex Type-->
               <!-- Group Element -->
               <xsl:element name="xs:element">
                    <xsl:attribute name="name"><xsl:text>Group</xsl:text></xsl:attribute>
                    <xsl:attribute name="substitutionGroup"><xsl:text>cross:Group</xsl:text></xsl:attribute>
                    <xsl:attribute name="type"><xsl:text>GroupType</xsl:text></xsl:attribute>
               </xsl:element>
               <!-- End Group Element -->
               <!-- Group Complex Type -->
               <xsl:element name="xs:complexType">
                    <xsl:attribute name="name"><xsl:text>GroupType</xsl:text></xsl:attribute>
                    <xsl:element name="xs:complexContent">
                         <xsl:element name="xs:extension">
                              <xsl:attribute name="base"><xsl:text>cross:GroupType</xsl:text></xsl:attribute>
                              <xsl:element name="xs:sequence">
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="ref"><xsl:text>Section</xsl:text></xsl:attribute>
                                        <xsl:attribute name="minOccurs"><xsl:text>0</xsl:text></xsl:attribute>
                                        <xsl:attribute name="maxOccurs"><xsl:text>unbounded</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Section)-->
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="name"><xsl:text>Annotations</xsl:text></xsl:attribute>
                                        <xsl:attribute name="type"><xsl:text>common:AnnotationsType</xsl:text></xsl:attribute>
                                        <xsl:attribute name="minOccurs"><xsl:text>0</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Annotations)-->
                              </xsl:element>
                              <!--Close xs:sequence-->
                              <!-- Group Attributes -->
                              <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/*[@crossSectionalAttachGroup='true' or @isFrequencyDimension='true']">
                                   <xsl:element name="xs:attribute">
                                        <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:if test="@codelist">
                                                  <xsl:value-of select="@codelist"/>
                                             </xsl:if>
                                             <xsl:if test="not(@codelist)">
                                                  <xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
                                        <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:attribute (of GroupType)-->
                              </xsl:for-each>
                         </xsl:element>
                         <!--Close xs:extension-->
                    </xsl:element>
                    <!--Close xs:complexContent-->
               </xsl:element>
               <!--Close xs:complexType-->
               <!-- End Group Complex Type -->
               <!-- Section Element -->
               <xsl:element name="xs:element">
                    <xsl:attribute name="name"><xsl:text>Section</xsl:text></xsl:attribute>
                    <xsl:attribute name="substitutionGroup"><xsl:text>cross:Section</xsl:text></xsl:attribute>
                    <xsl:attribute name="type"><xsl:text>SectionType</xsl:text></xsl:attribute>
               </xsl:element>
               <!-- End Secton Element -->
               <!-- Section Complex Type -->
               <xsl:element name="xs:complexType">
                    <xsl:attribute name="name"><xsl:text>SectionType</xsl:text></xsl:attribute>
                    <xsl:element name="xs:complexContent">
                         <xsl:element name="xs:extension">
                              <xsl:attribute name="base"><xsl:text>cross:SectionType</xsl:text></xsl:attribute>
                              <xsl:element name="xs:choice">
                                   <xsl:attribute name="minOccurs"><xsl:text>0</xsl:text></xsl:attribute>
                                   <xsl:attribute name="maxOccurs"><xsl:text>unbounded</xsl:text></xsl:attribute>
                                   <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:CrossSectionalMeasure">
                                        <xsl:element name="xs:element">
                                             <xsl:attribute name="ref"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                        </xsl:element>
                                        <!--Close xs:element (CrossSectionalMeasure)-->
                                   </xsl:for-each>
                                   
                                   <!-- @03/10/2009: If no XSMeasures, then use PrimaryMeasure -->
                                   <xsl:if test="not(/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:CrossSectionalMeasure)">
                                        <xsl:element name="xs:element">
                                             <xsl:attribute name="ref"><xsl:value-of select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:PrimaryMeasure/@conceptRef"/></xsl:attribute>
                                        </xsl:element>
                                        <!--Close xs:element (PrimaryMeasure)-->                                   
                                   </xsl:if>
                                   
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="name"><xsl:text>Annotations</xsl:text></xsl:attribute>
                                        <xsl:attribute name="type"><xsl:text>common:AnnotationsType</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Annotations)-->
                              </xsl:element>
                              <!--Close xs:choice-->
                              <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/*[@crossSectionalAttachSection='true']">
                                   <xsl:element name="xs:attribute">
                                        <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:if test="@codelist">
                                                  <xsl:value-of select="@codelist"/>
                                             </xsl:if>
                                             <xsl:if test="not(@codelist)">
                                                  <xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
                                        <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:attribute (of SectionType)-->
                              </xsl:for-each>
                         </xsl:element>
                         <!--Close xs:extension-->
                    </xsl:element>
                    <!--Close xs:complexContent-->
               </xsl:element>
               <!--Close xs:complexType-->
               <!-- End Section Complex Types -->
               <!-- Cross Sectional Measure Elements and Types -->
               <xsl:choose>
                    <xsl:when test="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:CrossSectionalMeasure">
                         <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:CrossSectionalMeasure">
                              <!--Commented and Replaced by the following line by SNi on 19/10/2007 <xsl:variable name="CSM" select="@code"/> -->
                              <xsl:variable name="CSM" select="@conceptRef"/>
                              <!-- Cross Sectional Measure Element -->
                              <xsl:element name="xs:element">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                   <xsl:attribute name="substitutionGroup"><xsl:text>cross:Obs</xsl:text></xsl:attribute>
                                   <xsl:attribute name="type"><xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text></xsl:attribute>
                              </xsl:element>
                              <!--Close xs:element (CrossSectionalMeasure)-->
                              <!-- Cross Sectional Measure Type -->
                              <xsl:element name="xs:complexType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text></xsl:attribute>
                                   <xsl:element name="xs:complexContent">
                                        <xsl:element name="xs:extension">
                                             <xsl:attribute name="base"><xsl:text>cross:ObsType</xsl:text></xsl:attribute>
                                             <xsl:element name="xs:sequence">
                                                  <xsl:element name="xs:element">
                                                       <xsl:attribute name="name"><xsl:text>Annotations</xsl:text></xsl:attribute>
                                                       <xsl:attribute name="type"><xsl:text>common:AnnotationsType</xsl:text></xsl:attribute>
                                                       <xsl:attribute name="minOccurs"><xsl:text>0</xsl:text></xsl:attribute>
                                                  </xsl:element>
                                                  <!--Close xs:element (Annotations)-->
                                             </xsl:element>
                                             <!--Close xs:sequence-->
                                             <!-- Dimension Attributes -->
                                             <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Dimension[@crossSectionalAttachObservation='true']">
                                                  <xsl:element name="xs:attribute">
                                                       <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                                       <!-- New Insertion @02/10/2009 -->
                                                       <xsl:attribute name="type">
                                                            <xsl:if test="@codelist">
                                                                 <xsl:value-of select="@codelist"/>
                                                            </xsl:if>
                                                            <xsl:if test="not(@codelist)">
                                                                 <xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text>
                                                            </xsl:if>
                                                       </xsl:attribute>
                                                       <!-- Replaced by above insertion
                                                       <xsl:attribute name="type"><xsl:value-of select="@codelist"/></xsl:attribute>-->
                                                       <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                                  </xsl:element>
                                                  <!--Close xs:attribute (of SectionType)-->
                                             </xsl:for-each>
                                             <!-- Attribute Attributes -->
                                             <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[@crossSectionalAttachObservation='true' and structure:AttachmentMeasure = $CSM]">
                                                  <xsl:element name="xs:attribute">
                                                       <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                                       <xsl:attribute name="type">
                                                            <xsl:if test="@codelist">
                                                                 <xsl:value-of select="@codelist"/>
                                                            </xsl:if>
                                                            <xsl:if test="not(@codelist)">
                                                                 <xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text>
                                                            </xsl:if>
                                                       </xsl:attribute>
                                                       <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                                  </xsl:element>
                                                  <!--Close xs:attribute (of SectionType)-->
                                             </xsl:for-each>
                                             <xsl:element name="xs:attribute">
                                                  <xsl:attribute name="name"><xsl:text>value</xsl:text></xsl:attribute>
                                                  <!-- New insertion @03/10/2009 -->
                                                  <xsl:attribute name="type">
                                                       <xsl:if test="@codelist">
                                                            <xsl:value-of select="@codelist"/>
                                                       </xsl:if>
                                                       <xsl:if test="not(@codelist)">
                                                            <xsl:value-of select="@conceptRef"/><xsl:text>ValueType</xsl:text>
                                                       </xsl:if>
                                                  </xsl:attribute>
                                                  <!-- Replaced by the above insertion
                                                  <xsl:attribute name="type"><xsl:text>xs:double</xsl:text></xsl:attribute>
                                                  -->
                                                  <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                             </xsl:element>
                                        </xsl:element>
                                        <!--Close xs:extension-->
                                   </xsl:element>
                                   <!--Close xs:complexContent-->
                              </xsl:element>
                         </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>
                         <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:PrimaryMeasure">
                              <!-- Primary Measure Element -->
                              <xsl:element name="xs:element">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                   <xsl:attribute name="type"><xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text></xsl:attribute>
                              </xsl:element>
                              <!--Close xs:element (PrimaryMeasure)-->
                              <!-- Primary Measure Type -->
                              <xsl:element name="xs:complexType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text></xsl:attribute>
                                   <xsl:element name="xs:complexContent">
                                        <xsl:element name="xs:extension">
                                             <xsl:attribute name="base"><xsl:text>cross:ObsType</xsl:text></xsl:attribute>
                                             <xsl:element name="xs:sequence">
                                                  <xsl:element name="xs:element">
                                                       <xsl:attribute name="name"><xsl:text>Annotations</xsl:text></xsl:attribute>
                                                       <xsl:attribute name="type"><xsl:text>common:AnnotationsType</xsl:text></xsl:attribute>
                                                       <xsl:attribute name="minOccurs"><xsl:text>0</xsl:text></xsl:attribute>
                                                  </xsl:element>
                                                  <!--Close xs:element (Annotations)-->
                                             </xsl:element>
                                             <!--Close xs:sequence-->
                                             <!-- Dimension Attributes -->
                                             <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Dimension[@crossSectionalAttachObservation='true']">
                                                  <xsl:element name="xs:attribute">
                                                       <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                                       <!-- New Insertion @02/10/2009 -->
                                                       <xsl:attribute name="type">
                                                            <xsl:if test="@codelist">
                                                                 <xsl:value-of select="@codelist"/>
                                                            </xsl:if>
                                                            <xsl:if test="not(@codelist)">
                                                                 <xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text>
                                                            </xsl:if>
                                                       </xsl:attribute>
                                                       <!-- Replaced by above insertion
                                                            <xsl:attribute name="type"><xsl:value-of select="@codelist"/></xsl:attribute>-->
                                                       <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                                  </xsl:element>
                                                  <!--Close xs:attribute (of SectionType)-->
                                             </xsl:for-each>
                                             <!-- Attribute Attributes -->
                                             <xsl:for-each select="/message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[@attachmentLevel='Observation']">
                                                  <xsl:element name="xs:attribute">
                                                       <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                                       <xsl:attribute name="type">
                                                            <xsl:if test="@codelist">
                                                                 <xsl:value-of select="@codelist"/>
                                                            </xsl:if>
                                                            <xsl:if test="not(@codelist)">
                                                                 <xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text>
                                                            </xsl:if>
                                                       </xsl:attribute>
                                                       <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                                  </xsl:element>
                                                  <!--Close xs:attribute (of SectionType)-->
                                             </xsl:for-each>
                                             <xsl:element name="xs:attribute">
                                                  <xsl:attribute name="name"><xsl:text>value</xsl:text></xsl:attribute>
                                                  <!-- New insertion @03/10/2009 -->
                                                  <xsl:attribute name="type">
                                                       <xsl:if test="@codelist">
                                                            <xsl:value-of select="@codelist"/>
                                                       </xsl:if>
                                                       <xsl:if test="not(@codelist)">
                                                            <xsl:value-of select="@conceptRef"/><xsl:text>ValueType</xsl:text>
                                                       </xsl:if>
                                                  </xsl:attribute>
                                                  <!-- Replaced by the above insertion
                                                       <xsl:attribute name="type"><xsl:text>xs:double</xsl:text></xsl:attribute>
                                                  -->
                                                  <xsl:attribute name="use"><xsl:text>optional</xsl:text></xsl:attribute>
                                             </xsl:element>
                                        </xsl:element>
                                        <!--Close xs:extension-->
                                   </xsl:element>
                                   <!--Close xs:complexContent-->
                              </xsl:element>
                         </xsl:for-each>
                    </xsl:otherwise>
               </xsl:choose>
               <!-- End Cross Sectional Measure Elements and Types -->
               <!-- Non Coded Attributes -->

               <!-- New insertion @02/10/2009 -->
               <xsl:for-each
                    select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:PrimaryMeasure[not(@codelist)] |
                    message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:CrossSectionalMeasure[not(@codelist)]">
                    <!-- Replaced by the above insertion
                         <xsl:for-each select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[not(@codelist)]">-->
                    
                    <!-- @03/10/2009 -->
                    <xsl:variable name="typePrefix" select="'ValueType'" />
                    
                    <xsl:choose>
                         <xsl:when test="structure:TextFormat/@TextType='Alpha'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>common:AlphaType</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>common:AlphaType</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:minLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='Num'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">xs:decimal</xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:totalDigits">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                        <xsl:if test="structure:TextFormat/@decimals">
                                             <xsl:element name="xs:fractionDigits">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@decimals"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='NumFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">xs:decimal</xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:totalDigits">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                                  <xsl:attribute name="fixed"><xsl:text>true</xsl:text></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                        <xsl:if test="structure:TextFormat/@decimals">
                                             <xsl:element name="xs:fractionDigits">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@decimals"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaNum'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>common:AlphaNumericType</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaNumFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>common:AlphaNumericType</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:minLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:otherwise>
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>xs:string</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:otherwise>
                    </xsl:choose>
               </xsl:for-each>
               <!-- End Non Coded Measures -->
               
               <xsl:for-each
                    select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[not(@codelist)] |                     
                    message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Dimension[not(@codelist)] ">
                    <!-- Replaced by the above insertion
                    <xsl:for-each select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[not(@codelist)]">-->
                    
                    <!-- @03/10/2009 -->
                    <xsl:variable name="typePrefix" select="'Type'" />

                    <xsl:choose>
                         <xsl:when test="structure:TextFormat/@TextType='Alpha'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>common:AlphaType</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>common:AlphaType</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:minLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='Num'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">xs:decimal</xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:totalDigits">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                        <xsl:if test="structure:TextFormat/@decimals">
                                             <xsl:element name="xs:fractionDigits">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@decimals"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='NumFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">xs:decimal</xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:totalDigits">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                                  <xsl:attribute name="fixed"><xsl:text>true</xsl:text></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                        <xsl:if test="structure:TextFormat/@decimals">
                                             <xsl:element name="xs:fractionDigits">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@decimals"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaNum'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>common:AlphaNumericType</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaNumFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>common:AlphaNumericType</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:minLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:otherwise>
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:value-of select="$typePrefix"/></xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base"><xsl:text>xs:string</xsl:text></xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value"><xsl:value-of select="structure:TextFormat/@length"/></xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:otherwise>
                    </xsl:choose>
               </xsl:for-each>
               <!-- End Non Coded Attributes -->
               <!-- Fix for TimeDimension dimensions of a Key Family component custom defined types -->
               <xsl:for-each select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:TimeDimension[not(@codelist)]">
                   <xsl:element name="xs:simpleType">
                        <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/><xsl:text>Type</xsl:text></xsl:attribute>
                        <xsl:element name="xs:restriction">
                             <xsl:attribute name="base"><xsl:text>common:TimePeriodType</xsl:text></xsl:attribute>
                        </xsl:element>
                   </xsl:element>
              </xsl:for-each>
              <!-- -->
               <!-- Code Lists -->
               <xsl:for-each select="message:Structure/message:CodeLists/structure:CodeList">
                    <xsl:element name="xs:simpleType">
                         <xsl:attribute name="name"><xsl:value-of select="@id"/></xsl:attribute>
                         <xsl:element name="xs:restriction">
                              <xsl:attribute name="base">xs:string</xsl:attribute>
                              <xsl:for-each select="structure:Code">
                                   <xsl:element name="xs:enumeration">
                                        <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
                                        <xsl:element name="xs:annotation">
                                             <xsl:element name="xs:documentation">
                                                  <xsl:for-each select="structure:Description/@*">
                                                       <xsl:copy/>
                                                  </xsl:for-each>
                                                  <xsl:value-of select="structure:Description"/>
                                             </xsl:element>
                                             <!--Close xs:documentation-->
                                        </xsl:element>
                                        <!--Close xs:annotation-->
                                   </xsl:element>
                                   <!--Close xs:enumeration-->
                              </xsl:for-each>
                         </xsl:element>
                         <!--Close xs:restriction-->
                    </xsl:element>
                    <!--Close xs:simpleType-->
               </xsl:for-each>
               <!-- End Code Lists -->
          </xs:schema>
          <!--Close xs:schema-->
     </xsl:template>
</xsl:stylesheet>