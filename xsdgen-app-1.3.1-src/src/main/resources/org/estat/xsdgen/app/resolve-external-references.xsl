<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:message="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message"
                xmlns:structure="http://www.sdmx.org/resources/sdmxml/schemas/v2_1/structure"
                xmlns:func="org:estat:xsdgen:app"
                version="2.0">

    <xsl:output method="html" encoding="UTF-8" indent="yes" media-type="text/xml"/>

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
    <xsl:function name="func:GetFullItem">
        <!-- The item, which may be full or it may be defined externally. -->
        <xsl:param name="itemElm"/>

        <xsl:variable name="itemRTF">
            <xsl:choose>
                <xsl:when test="$itemElm/@isExternalReference=true()">
                    <xsl:variable name="url"
                                  select="if (empty($itemElm/@structureURL)) then $itemElm/@serviceURL else $itemElm/@structureURL"/>
                    <xsl:if test="empty($url)">
                        <xsl:message terminate="yes">
                            <xsl:text>ERROR: missing source URL for externally defined item</xsl:text>
                            <xsl:value-of select="func:ItemToString($itemElm)"/>
                        </xsl:message>
                    </xsl:if>
                    <xsl:variable name="externItemElm"
                                  select="document($url, root($itemElm))/message:Structure/message:Structures/*/*[(@id=$itemElm/@id) and (@agencyID=$itemElm/@agencyID) and (empty($itemElm/@version) or (@version=$itemElm/@version))]"/>
                    <xsl:choose>
                        <xsl:when test="count($externItemElm)!=1">
                            <xsl:message terminate="yes">
                                <xsl:text>ERROR: failed to find exactly one</xsl:text>
                                <xsl:value-of select="func:ItemToString($itemElm)"/>
                                <xsl:text>item inside document retrieved from URL</xsl:text>
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

    <!-- The identity template from XSLT 1.0 W3C Recommendation: http://www.w3.org/TR/xslt#copying -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- Resolves the external codelists. -->
    <xsl:template match="structure:Codelist[@isExternalReference=true()]">
        <xsl:copy-of select="func:GetFullItem(.)"/>
    </xsl:template>

    <!-- Resolves the external concept schemes. -->
    <xsl:template match="structure:ConceptScheme[@isExternalReference=true()]">
        <xsl:copy-of select="func:GetFullItem(.)"/>
    </xsl:template>
</xsl:stylesheet>
