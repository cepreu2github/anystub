package org.anystub.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class StubCallableStatement extends StubPreparedStatement implements CallableStatement {
    public StubCallableStatement(StubConnection stubConnection) {
        super(true, stubConnection);
    }

    @Override
    public void registerOutParameter(int i, int i1) throws SQLException {

    }

    @Override
    public void registerOutParameter(int i, int i1, int i2) throws SQLException {

    }

    @Override
    public boolean wasNull() throws SQLException {
        return false;
    }

    @Override
    public String getString(int i) throws SQLException {
        return null;
    }

    @Override
    public boolean getBoolean(int i) throws SQLException {
        return false;
    }

    @Override
    public byte getByte(int i) throws SQLException {
        return 0;
    }

    @Override
    public short getShort(int i) throws SQLException {
        return 0;
    }

    @Override
    public int getInt(int i) throws SQLException {
        return 0;
    }

    @Override
    public long getLong(int i) throws SQLException {
        return 0;
    }

    @Override
    public float getFloat(int i) throws SQLException {
        return 0;
    }

    @Override
    public double getDouble(int i) throws SQLException {
        return 0;
    }

    @Override
    public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
        return null;
    }

    @Override
    public byte[] getBytes(int i) throws SQLException {
        return new byte[0];
    }

    @Override
    public Date getDate(int i) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(int i) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int i) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(int i) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int i) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(int i) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(int i) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(int i) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(int i) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(int i, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(int i, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public void registerOutParameter(int i, int i1, String s) throws SQLException {

    }

    @Override
    public void registerOutParameter(String s, int i) throws SQLException {

    }

    @Override
    public void registerOutParameter(String s, int i, int i1) throws SQLException {

    }

    @Override
    public void registerOutParameter(String s, int i, String s1) throws SQLException {

    }

    @Override
    public URL getURL(int i) throws SQLException {
        return null;
    }

    @Override
    public void setURL(String s, URL url) throws SQLException {

    }

    @Override
    public void setNull(String s, int i) throws SQLException {

    }

    @Override
    public void setBoolean(String s, boolean b) throws SQLException {

    }

    @Override
    public void setByte(String s, byte b) throws SQLException {

    }

    @Override
    public void setShort(String s, short i) throws SQLException {

    }

    @Override
    public void setInt(String s, int i) throws SQLException {

    }

    @Override
    public void setLong(String s, long l) throws SQLException {

    }

    @Override
    public void setFloat(String s, float v) throws SQLException {

    }

    @Override
    public void setDouble(String s, double v) throws SQLException {

    }

    @Override
    public void setBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {

    }

    @Override
    public void setString(String s, String s1) throws SQLException {

    }

    @Override
    public void setBytes(String s, byte[] bytes) throws SQLException {

    }

    @Override
    public void setDate(String s, Date date) throws SQLException {

    }

    @Override
    public void setTime(String s, Time time) throws SQLException {

    }

    @Override
    public void setTimestamp(String s, Timestamp timestamp) throws SQLException {

    }

    @Override
    public void setAsciiStream(String s, InputStream inputStream, int i) throws SQLException {

    }

    @Override
    public void setBinaryStream(String s, InputStream inputStream, int i) throws SQLException {

    }

    @Override
    public void setObject(String s, Object o, int i, int i1) throws SQLException {

    }

    @Override
    public void setObject(String s, Object o, int i) throws SQLException {

    }

    @Override
    public void setObject(String s, Object o) throws SQLException {

    }

    @Override
    public void setCharacterStream(String s, Reader reader, int i) throws SQLException {

    }

    @Override
    public void setDate(String s, Date date, Calendar calendar) throws SQLException {

    }

    @Override
    public void setTime(String s, Time time, Calendar calendar) throws SQLException {

    }

    @Override
    public void setTimestamp(String s, Timestamp timestamp, Calendar calendar) throws SQLException {

    }

    @Override
    public void setNull(String s, int i, String s1) throws SQLException {

    }

    @Override
    public String getString(String s) throws SQLException {
        return null;
    }

    @Override
    public boolean getBoolean(String s) throws SQLException {
        return false;
    }

    @Override
    public byte getByte(String s) throws SQLException {
        return 0;
    }

    @Override
    public short getShort(String s) throws SQLException {
        return 0;
    }

    @Override
    public int getInt(String s) throws SQLException {
        return 0;
    }

    @Override
    public long getLong(String s) throws SQLException {
        return 0;
    }

    @Override
    public float getFloat(String s) throws SQLException {
        return 0;
    }

    @Override
    public double getDouble(String s) throws SQLException {
        return 0;
    }

    @Override
    public byte[] getBytes(String s) throws SQLException {
        return new byte[0];
    }

    @Override
    public Date getDate(String s) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(String s) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String s) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(String s) throws SQLException {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String s) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(String s, Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(String s) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(String s) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(String s) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(String s) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(String s, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(String s, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
        return null;
    }

    @Override
    public URL getURL(String s) throws SQLException {
        return null;
    }

    @Override
    public RowId getRowId(int i) throws SQLException {
        return null;
    }

    @Override
    public RowId getRowId(String s) throws SQLException {
        return null;
    }

    @Override
    public void setRowId(String s, RowId rowId) throws SQLException {

    }

    @Override
    public void setNString(String s, String s1) throws SQLException {

    }

    @Override
    public void setNCharacterStream(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void setNClob(String s, NClob nClob) throws SQLException {

    }

    @Override
    public void setClob(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void setBlob(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void setNClob(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public NClob getNClob(int i) throws SQLException {
        return null;
    }

    @Override
    public NClob getNClob(String s) throws SQLException {
        return null;
    }

    @Override
    public void setSQLXML(String s, SQLXML sqlxml) throws SQLException {

    }

    @Override
    public SQLXML getSQLXML(int i) throws SQLException {
        return null;
    }

    @Override
    public SQLXML getSQLXML(String s) throws SQLException {
        return null;
    }

    @Override
    public String getNString(int i) throws SQLException {
        return null;
    }

    @Override
    public String getNString(String s) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(int i) throws SQLException {
        return null;
    }

    @Override
    public Reader getNCharacterStream(String s) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream(int i) throws SQLException {
        return null;
    }

    @Override
    public Reader getCharacterStream(String s) throws SQLException {
        return null;
    }

    @Override
    public void setBlob(String s, Blob blob) throws SQLException {

    }

    @Override
    public void setClob(String s, Clob clob) throws SQLException {

    }

    @Override
    public void setAsciiStream(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void setBinaryStream(String s, InputStream inputStream, long l) throws SQLException {

    }

    @Override
    public void setCharacterStream(String s, Reader reader, long l) throws SQLException {

    }

    @Override
    public void setAsciiStream(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void setBinaryStream(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void setCharacterStream(String s, Reader reader) throws SQLException {

    }

    @Override
    public void setNCharacterStream(String s, Reader reader) throws SQLException {

    }

    @Override
    public void setClob(String s, Reader reader) throws SQLException {

    }

    @Override
    public void setBlob(String s, InputStream inputStream) throws SQLException {

    }

    @Override
    public void setNClob(String s, Reader reader) throws SQLException {

    }

    @Override
    public <T> T getObject(int i, Class<T> aClass) throws SQLException {
        return null;
    }

    @Override
    public <T> T getObject(String s, Class<T> aClass) throws SQLException {
        return null;
    }
}
