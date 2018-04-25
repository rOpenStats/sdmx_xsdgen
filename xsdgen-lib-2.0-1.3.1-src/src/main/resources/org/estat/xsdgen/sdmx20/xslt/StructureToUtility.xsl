<?xml version="1.0"?>
<!-- Updated on 02/10/2009 to support uncoded Dimensions & PrimaryMeasure. Shipped with Eurostat's SDMX Registry v3.3.16 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:common="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/common"
     xmlns:message="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message"
     xmlns:structure="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure"
     xmlns:utility="http://www.SDMX.org/resources/SDMXML/schemas/v2_0/utility" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="structure message" version="2.0">
     <xsl:output method="xml" indent="yes"/>
     <xsl:param name="Namespace">nameSpaceHolder</xsl:param>
     <xsl:param name="CommonURI">SDMXCommon.xsd</xsl:param>
     <xsl:param name="UtilityURI">SDMXUtilityData.xsd</xsl:param>
     <xsl:param name="KeyFamily">1</xsl:param>
     <xsl:template match="/">
          <xs:schema>
               <xsl:namespace name="">
                    <xsl:value-of select="//structure:KeyFamily/@urn"/>
                    <xsl:value-of select="$Namespace"/>
               </xsl:namespace>
               <xsl:attribute name="targetNamespace">
                    <xsl:value-of select="//structure:KeyFamily/@urn"/>
                    <xsl:value-of select="$Namespace"/>
               </xsl:attribute>
               <xsl:attribute name="elementFormDefault">
                    <xsl:text>qualified</xsl:text>
               </xsl:attribute>
               <xsl:attribute name="attributeFormDefault">
                    <xsl:text>unqualified</xsl:text>
               </xsl:attribute>
               <xsl:element name="xs:import">
                    <xsl:attribute name="namespace">
                         <xsl:text>http://www.SDMX.org/resources/SDMXML/schemas/v2_0/utility</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="schemaLocation">
                         <xsl:value-of select="$UtilityURI"/>
                    </xsl:attribute>
               </xsl:element>
               <xsl:element name="xs:import">
                    <xsl:attribute name="namespace">
                         <xsl:text>http://www.SDMX.org/resources/SDMXML/schemas/v2_0/common</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="schemaLocation">
                         <xsl:value-of select="$CommonURI"/>
                    </xsl:attribute>
               </xsl:element>
               <xsl:element name="xs:element">
                    <xsl:attribute name="name">
                         <xsl:text>DataSet</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="type">
                         <xsl:text>DataSetType</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="substitutionGroup">
                         <xsl:text>utility:DataSet</xsl:text>
                    </xsl:attribute>
               </xsl:element>
               <!-- Data Set Complex Type-->
               <xsl:element name="xs:complexType">
                    <xsl:attribute name="name">
                         <xsl:text>DataSetType</xsl:text>
                    </xsl:attribute>
                    <xsl:element name="xs:complexContent">
                         <xsl:element name="xs:extension">
                              <xsl:attribute name="base">
                                   <xsl:text>utility:DataSetType</xsl:text>
                              </xsl:attribute>
                              <xsl:element name="xs:sequence">
                                   <xsl:element name="xs:choice">
                                        <xsl:attribute name="maxOccurs">
                                             <xsl:text>unbounded</xsl:text>
                                        </xsl:attribute>
                                        <xsl:for-each
                                             select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components">
                                             <xsl:choose>
                                                  <xsl:when test="structure:Group">
                                                       <xsl:for-each select="structure:Group">
                                                            <xsl:element name="xs:element">
                                                                 <xsl:attribute name="ref">
                                                                      <xsl:value-of select="@id"/>
                                                                 </xsl:attribute>
                                                            </xsl:element>
                                                            <!--Close xs:element (Group)-->
                                                       </xsl:for-each>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                       <xsl:element name="xs:element">
                                                            <xsl:attribute name="ref">
                                                                 <xsl:text>Series</xsl:text>
                                                            </xsl:attribute>
                                                       </xsl:element>
                                                       <!--Close xs:element (Series)-->
                                                  </xsl:otherwise>
                                             </xsl:choose>
                                        </xsl:for-each>
                                   </xsl:element>
                                   <!--Close xs:choice-->
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="name">
                                             <xsl:text>Annotations</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:text>common:AnnotationsType</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="minOccurs">
                                             <xsl:text>0</xsl:text>
                                        </xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Annotations)-->
                              </xsl:element>
                              <!--Close xs:sequence-->
                              <xsl:for-each
                                   select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[@attachmentLevel='DataSet']">
                                   <xsl:element name="xs:attribute">
                                        <xsl:attribute name="name">
                                             <xsl:value-of select="@conceptRef"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:if test="@codelist">
                                                  <xsl:value-of select="@codelist"/>
                                             </xsl:if>
                                             <xsl:if test="not(@codelist)">
                                                  <xsl:value-of select="@conceptRef"/>
                                                  <xsl:text>Type</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
                                        <xsl:attribute name="use">
                                             <xsl:if test="@assignmentStatus='Mandatory'">
                                                  <xsl:text>required</xsl:text>
                                             </xsl:if>
                                             <xsl:if test="@assignmentStatus='Conditional'">
                                                  <xsl:text>optional</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
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
               <!-- Group Elements -->
               <xsl:for-each select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Group">
                    <xsl:element name="xs:element">
                         <xsl:attribute name="name">
                              <xsl:value-of select="@id"/>
                         </xsl:attribute>
                         <xsl:attribute name="substitutionGroup">
                              <xsl:text>utility:Group</xsl:text>
                         </xsl:attribute>
                         <xsl:attribute name="type">
                              <xsl:value-of select="@id"/>
                              <xsl:text>Type</xsl:text>
                         </xsl:attribute>
                    </xsl:element>
               </xsl:for-each>
               <!-- End Group Elements -->
               <!-- Group Complex Types -->
               <xsl:for-each select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Group">
                    <xsl:element name="xs:complexType">
                         <xsl:attribute name="name">
                              <xsl:value-of select="@id"/>
                              <xsl:text>Type</xsl:text>
                         </xsl:attribute>
                         <xsl:element name="xs:complexContent">
                              <xsl:element name="xs:extension">
                                   <xsl:attribute name="base">
                                        <xsl:text>utility:GroupType</xsl:text>
                                   </xsl:attribute>
                                   <xsl:element name="xs:sequence">
                                        <xsl:element name="xs:element">
                                             <xsl:attribute name="name">
                                                  <xsl:text>Series</xsl:text>
                                             </xsl:attribute>
                                             <xsl:attribute name="type">
                                                  <xsl:text>SeriesType</xsl:text>
                                             </xsl:attribute>
                                             <xsl:attribute name="maxOccurs">
                                                  <xsl:text>unbounded</xsl:text>
                                             </xsl:attribute>
                                        </xsl:element>
                                        <!--Close xs:element (Series)-->
                                        <xsl:element name="xs:element">
                                             <xsl:attribute name="name">
                                                  <xsl:text>Annotations</xsl:text>
                                             </xsl:attribute>
                                             <xsl:attribute name="type">
                                                  <xsl:text>common:AnnotationsType</xsl:text>
                                             </xsl:attribute>
                                             <xsl:attribute name="minOccurs">
                                                  <xsl:text>0</xsl:text>
                                             </xsl:attribute>
                                        </xsl:element>
                                        <!--Close xs:element (Annotations)-->
                                   </xsl:element>
                                   <!--Close xs:sequence-->
                                   <xsl:for-each select="../structure:Attribute[@attachmentLevel='Group']">
                                        <xsl:if test="structure:AttachmentGroup=../structure:Group/@id">
                                             <xsl:element name="xs:attribute">
                                                  <xsl:attribute name="name">
                                                       <xsl:value-of select="@conceptRef"/>
                                                  </xsl:attribute>
                                                  <xsl:attribute name="type">
                                                       <xsl:if test="@codelist">
                                                            <xsl:value-of select="@codelist"/>
                                                       </xsl:if>
                                                       <xsl:if test="not(@codelist)">
                                                            <xsl:value-of select="@conceptRef"/>
                                                            <xsl:text>Type</xsl:text>
                                                       </xsl:if>
                                                  </xsl:attribute>
                                                  <xsl:attribute name="use">
                                                       <xsl:if test="@assignmentStatus='Mandatory'">
                                                            <xsl:text>required</xsl:text>
                                                       </xsl:if>
                                                       <xsl:if test="@assignmentStatus='Conditional'">
                                                            <xsl:text>optional</xsl:text>
                                                       </xsl:if>
                                                  </xsl:attribute>
                                             </xsl:element>
                                             <!--Close xs:attribute (of <Group>Type)-->
                                        </xsl:if>
                                   </xsl:for-each>
                              </xsl:element>
                              <!--Close xs:extension-->
                         </xsl:element>
                         <!--Close xs:complexContent-->
                    </xsl:element>
                    <!--Close xs:complexType-->
               </xsl:for-each>
               <!-- End Group Complex Types -->
               <!-- Series Element -->
               <xsl:element name="xs:element">
                    <xsl:attribute name="name">
                         <xsl:text>Series</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="substitutionGroup">
                         <xsl:text>utility:Series</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="type">
                         <xsl:text>SeriesType</xsl:text>
                    </xsl:attribute>
               </xsl:element>
               <!-- End Series Element -->
               <!-- Series Complex Type -->
               <xsl:element name="xs:complexType">
                    <xsl:attribute name="name">
                         <xsl:text>SeriesType</xsl:text>
                    </xsl:attribute>
                    <xsl:element name="xs:complexContent">
                         <xsl:element name="xs:extension">
                              <xsl:attribute name="base">
                                   <xsl:text>utility:SeriesType</xsl:text>
                              </xsl:attribute>
                              <xsl:element name="xs:sequence">
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="ref">
                                             <xsl:text>Key</xsl:text>
                                        </xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Key)-->
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="ref">
                                             <xsl:text>Obs</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="maxOccurs">
                                             <xsl:text>unbounded</xsl:text>
                                        </xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Obs)-->
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="name">
                                             <xsl:text>Annotations</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:text>common:AnnotationsType</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="minOccurs">
                                             <xsl:text>0</xsl:text>
                                        </xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Annotations)-->
                              </xsl:element>
                              <!--Close xs:sequence-->
                              <xsl:for-each
                                   select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[@attachmentLevel='Series']">
                                   <xsl:element name="xs:attribute">
                                        <xsl:attribute name="name">
                                             <xsl:value-of select="@conceptRef"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:if test="@codelist">
                                                  <xsl:value-of select="@codelist"/>
                                             </xsl:if>
                                             <xsl:if test="not(@codelist)">
                                                  <xsl:value-of select="@conceptRef"/>
                                                  <xsl:text>Type</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
                                        <xsl:attribute name="use">
                                             <xsl:if test="@assignmentStatus='Mandatory'">
                                                  <xsl:text>required</xsl:text>
                                             </xsl:if>
                                             <xsl:if test="@assignmentStatus='Conditional'">
                                                  <xsl:text>optional</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:attribute (of SeriesType)-->
                              </xsl:for-each>
                         </xsl:element>
                         <!--Close xs:extension-->
                    </xsl:element>
                    <!--Close xs:complexContent-->
               </xsl:element>
               <!--Close xs:complexType-->
               <!-- End Series Complex Types -->
               <!-- Key Element -->
               <xsl:element name="xs:element">
                    <xsl:attribute name="name">
                         <xsl:text>Key</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="substitutionGroup">
                         <xsl:text>utility:Key</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="type">
                         <xsl:text>KeyType</xsl:text>
                    </xsl:attribute>
               </xsl:element>
               <!-- End Key Element -->
               <!-- Key Complex Type -->
               <xsl:element name="xs:complexType">
                    <xsl:attribute name="name">
                         <xsl:text>KeyType</xsl:text>
                    </xsl:attribute>
                    <xsl:element name="xs:complexContent">
                         <xsl:element name="xs:extension">
                              <xsl:attribute name="base">
                                   <xsl:text>utility:KeyType</xsl:text>
                              </xsl:attribute>
                              <xsl:element name="xs:sequence">
                                   <xsl:for-each
                                        select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Dimension">
                                        <!-- New insertion @02/10/2009 -->
                                        <xsl:element name="xs:element">
                                             <xsl:attribute name="name">
                                                  <xsl:value-of select="@conceptRef"/>
                                             </xsl:attribute>
                                             <xsl:attribute name="type">
                                                  <xsl:if test="@codelist">
                                                       <xsl:value-of select="@codelist"/>
                                                  </xsl:if>
                                                  <xsl:if test="not(@codelist)">
                                                       <xsl:value-of select="@conceptRef"/>
                                                       <xsl:text>Type</xsl:text>
                                                  </xsl:if>
                                             </xsl:attribute>
                                        </xsl:element>
                                        <!-- Replaced with the above insertion, in order to support uncoded Dimensions
                                             <xsl:element name="xs:element">
                                             <xsl:attribute name="name"><xsl:value-of select="@conceptRef"/></xsl:attribute>
                                             <xsl:attribute name="type"><xsl:value-of select="@codelist"/></xsl:attribute>
                                        </xsl:element>-->
                                        <!--Close xs:element (Key Dimension)-->
                                   </xsl:for-each>
                              </xsl:element>
                              <!--Close xs:sequence-->
                         </xsl:element>
                         <!--Close xs:extension-->
                    </xsl:element>
                    <!--Close xs:complexContent-->
               </xsl:element>
               <!--Close xs:complexType-->
               <!-- End Key Complex Types -->
               <!-- Obs Element -->
               <xsl:element name="xs:element">
                    <xsl:attribute name="name">
                         <xsl:text>Obs</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="substitutionGroup">
                         <xsl:text>utility:Obs</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="type">
                         <xsl:text>ObsType</xsl:text>
                    </xsl:attribute>
               </xsl:element>
               <!-- End Obs Element -->
               <!-- Obs Complex Type -->
               <xsl:element name="xs:complexType">
                    <xsl:attribute name="name">
                         <xsl:text>ObsType</xsl:text>
                    </xsl:attribute>
                    <xsl:element name="xs:complexContent">
                         <xsl:element name="xs:extension">
                              <xsl:attribute name="base">
                                   <xsl:text>utility:ObsType</xsl:text>
                              </xsl:attribute>
                              <xsl:element name="xs:sequence">
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="name">
                                             <xsl:value-of
                                                  select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:TimeDimension/@conceptRef"
                                             />
                                        </xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:text>common:TimePeriodType</xsl:text>
                                        </xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Time Dimension)-->
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="name">
                                             <xsl:value-of
                                                  select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:PrimaryMeasure/@conceptRef"
                                             />
                                        </xsl:attribute>
                                        <!-- New Insertion @02/10/2009 -->
                                        <xsl:variable name="primaryMeasure"
                                             select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:PrimaryMeasure"/>
                                        <xsl:attribute name="type">
                                             <xsl:choose>
                                                  <xsl:when test="$primaryMeasure/@codelist">
                                                       <xsl:value-of select="$primaryMeasure/@codelist"/>
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                       <xsl:value-of select="$primaryMeasure/@conceptRef"/>
                                                       <xsl:text>Type</xsl:text>
                                                  </xsl:otherwise>
                                             </xsl:choose>
                                        </xsl:attribute>
                                        <!-- Replaced by the above insertion
                                             <xsl:attribute name="type"><xsl:text>xs:double</xsl:text></xsl:attribute>-->
                                   </xsl:element>
                                   <!--Close xs:element (Measure Dimension)-->
                                   <xsl:element name="xs:element">
                                        <xsl:attribute name="name">
                                             <xsl:text>Annotations</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:text>common:AnnotationsType</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="minOccurs">
                                             <xsl:text>0</xsl:text>
                                        </xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:element (Annotations)-->
                              </xsl:element>
                              <!--Close xs:sequence-->
                              <xsl:for-each
                                   select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[@attachmentLevel='Observation']">
                                   <xsl:element name="xs:attribute">
                                        <xsl:attribute name="name">
                                             <xsl:value-of select="@conceptRef"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="type">
                                             <xsl:if test="@codelist">
                                                  <xsl:value-of select="@codelist"/>
                                             </xsl:if>
                                             <xsl:if test="not(@codelist)">
                                                  <xsl:value-of select="@conceptRef"/>
                                                  <xsl:text>Type</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
                                        <xsl:attribute name="use">
                                             <xsl:if test="@assignmentStatus='Mandatory'">
                                                  <xsl:text>required</xsl:text>
                                             </xsl:if>
                                             <xsl:if test="@assignmentStatus='Conditional'">
                                                  <xsl:text>optional</xsl:text>
                                             </xsl:if>
                                        </xsl:attribute>
                                   </xsl:element>
                                   <!--Close xs:attribute (of ObservationType)-->
                              </xsl:for-each>
                         </xsl:element>
                         <!--Close xs:extension-->
                    </xsl:element>
                    <!--Close xs:complexContent-->
               </xsl:element>
               <!--Close xs:complexType-->
               <!-- End Obs Complex Types -->
               <!-- Non Coded Attributes -->
               <!-- New insertion @02/10/2009 -->
               <xsl:for-each
                    select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:PrimaryMeasure[not(@codelist)] |                      
                    message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[not(@codelist)] |                     
                    message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Dimension[not(@codelist)] ">
                    <!-- Replaced by the above insertion
               <xsl:for-each select="message:Structure/message:KeyFamilies/structure:KeyFamily[$KeyFamily]/structure:Components/structure:Attribute[not(@codelist)]">-->
                    <xsl:choose>
                         <xsl:when test="structure:TextFormat/@TextType='Alpha'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name">
                                        <xsl:value-of select="@conceptRef"/>
                                        <xsl:text>Type</xsl:text>
                                   </xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">
                                             <xsl:text>common:AlphaType</xsl:text>
                                        </xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name">
                                        <xsl:value-of select="@conceptRef"/>
                                        <xsl:text>Type</xsl:text>
                                   </xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">
                                             <xsl:text>common:AlphaType</xsl:text>
                                        </xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:minLength">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='Num'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name">
                                        <xsl:value-of select="@conceptRef"/>
                                        <xsl:text>Type</xsl:text>
                                   </xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">xs:decimal</xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:totalDigits">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                        <xsl:if test="structure:TextFormat/@decimals">
                                             <xsl:element name="xs:fractionDigits">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@decimals"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='NumFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name">
                                        <xsl:value-of select="@conceptRef"/>
                                        <xsl:text>Type</xsl:text>
                                   </xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">xs:decimal</xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:totalDigits">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                                  <xsl:attribute name="fixed">
                                                       <xsl:text>true</xsl:text>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                        <xsl:if test="structure:TextFormat/@decimals">
                                             <xsl:element name="xs:fractionDigits">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@decimals"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaNum'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name">
                                        <xsl:value-of select="@conceptRef"/>
                                        <xsl:text>Type</xsl:text>
                                   </xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">
                                             <xsl:text>common:AlphaNumericType</xsl:text>
                                        </xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:when test="structure:TextFormat/@TextType='AlphaNumFixed'">
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name">
                                        <xsl:value-of select="@conceptRef"/>
                                        <xsl:text>Type</xsl:text>
                                   </xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">
                                             <xsl:text>common:AlphaNumericType</xsl:text>
                                        </xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:minLength">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:when>
                         <xsl:otherwise>
                              <xsl:element name="xs:simpleType">
                                   <xsl:attribute name="name">
                                        <xsl:value-of select="@conceptRef"/>
                                        <xsl:text>Type</xsl:text>
                                   </xsl:attribute>
                                   <xsl:element name="xs:restriction">
                                        <xsl:attribute name="base">
                                             <xsl:text>xs:string</xsl:text>
                                        </xsl:attribute>
                                        <xsl:if test="structure:TextFormat/@length">
                                             <xsl:element name="xs:maxLength">
                                                  <xsl:attribute name="value">
                                                       <xsl:value-of select="structure:TextFormat/@length"/>
                                                  </xsl:attribute>
                                             </xsl:element>
                                        </xsl:if>
                                   </xsl:element>
                              </xsl:element>
                         </xsl:otherwise>
                    </xsl:choose>
               </xsl:for-each>
               <!-- End Non Coded Attributes -->
               <!-- Code Lists -->
               <xsl:for-each select="message:Structure/message:CodeLists/structure:CodeList">
                    <xsl:element name="xs:simpleType">
                         <xsl:attribute name="name">
                              <xsl:value-of select="@id"/>
                         </xsl:attribute>
                         <xsl:element name="xs:restriction">
                              <xsl:attribute name="base">xs:string</xsl:attribute>
                              <xsl:for-each select="structure:Code">
                                   <xsl:element name="xs:enumeration">
                                        <xsl:attribute name="value">
                                             <xsl:value-of select="@value"/>
                                        </xsl:attribute>
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