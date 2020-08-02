/*
 * This file is generated by jOOQ.
 */
package org.observertc.webrtc.service.jooq.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.observertc.webrtc.service.jooq.Indexes;
import org.observertc.webrtc.service.jooq.Keys;
import org.observertc.webrtc.service.jooq.Webrtcobserver;
import org.observertc.webrtc.service.jooq.tables.records.PeerconnectionssrcsRecord;


/**
 * A table to map peer connections to SSRCs
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Peerconnectionssrcs extends TableImpl<PeerconnectionssrcsRecord> {

    private static final long serialVersionUID = 173326778;

    /**
     * The reference instance of <code>WebRTCObserver.PeerConnectionSSRCs</code>
     */
    public static final Peerconnectionssrcs PEERCONNECTIONSSRCS = new Peerconnectionssrcs();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PeerconnectionssrcsRecord> getRecordType() {
        return PeerconnectionssrcsRecord.class;
    }

    /**
     * The column <code>WebRTCObserver.PeerConnectionSSRCs.observerUUID</code>. The UUID of the observer the SSRC belongs to
     */
    public final TableField<PeerconnectionssrcsRecord, byte[]> OBSERVERUUID = createField(DSL.name("observerUUID"), org.jooq.impl.SQLDataType.BINARY(16).nullable(false), this, "The UUID of the observer the SSRC belongs to");

    /**
     * The column <code>WebRTCObserver.PeerConnectionSSRCs.SSRC</code>. The SSRC identifier
     */
    public final TableField<PeerconnectionssrcsRecord, Long> SSRC = createField(DSL.name("SSRC"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "The SSRC identifier");

    /**
     * The column <code>WebRTCObserver.PeerConnectionSSRCs.peerConnectionUUID</code>. The UUID of the peer connection the SSRC belongs to
     */
    public final TableField<PeerconnectionssrcsRecord, byte[]> PEERCONNECTIONUUID = createField(DSL.name("peerConnectionUUID"), org.jooq.impl.SQLDataType.BINARY(16).nullable(false), this, "The UUID of the peer connection the SSRC belongs to");

    /**
     * The column <code>WebRTCObserver.PeerConnectionSSRCs.updated</code>.
     */
    public final TableField<PeerconnectionssrcsRecord, LocalDateTime> UPDATED = createField(DSL.name("updated"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>WebRTCObserver.PeerConnectionSSRCs.browserID</code>.
     */
    public final TableField<PeerconnectionssrcsRecord, String> BROWSERID = createField(DSL.name("browserID"), org.jooq.impl.SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>WebRTCObserver.PeerConnectionSSRCs.timeZone</code>.
     */
    public final TableField<PeerconnectionssrcsRecord, String> TIMEZONE = createField(DSL.name("timeZone"), org.jooq.impl.SQLDataType.VARCHAR(64), this, "");

    /**
     * Create a <code>WebRTCObserver.PeerConnectionSSRCs</code> table reference
     */
    public Peerconnectionssrcs() {
        this(DSL.name("PeerConnectionSSRCs"), null);
    }

    /**
     * Create an aliased <code>WebRTCObserver.PeerConnectionSSRCs</code> table reference
     */
    public Peerconnectionssrcs(String alias) {
        this(DSL.name(alias), PEERCONNECTIONSSRCS);
    }

    /**
     * Create an aliased <code>WebRTCObserver.PeerConnectionSSRCs</code> table reference
     */
    public Peerconnectionssrcs(Name alias) {
        this(alias, PEERCONNECTIONSSRCS);
    }

    private Peerconnectionssrcs(Name alias, Table<PeerconnectionssrcsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Peerconnectionssrcs(Name alias, Table<PeerconnectionssrcsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("A table to map peer connections to SSRCs"), TableOptions.table());
    }

    public <O extends Record> Peerconnectionssrcs(Table<O> child, ForeignKey<O, PeerconnectionssrcsRecord> key) {
        super(child, key, PEERCONNECTIONSSRCS);
    }

    @Override
    public Schema getSchema() {
        return Webrtcobserver.WEBRTCOBSERVER;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PEERCONNECTIONSSRCS_PEERCONNECTIONSSRCS_OBSERVER_SSRC_INDEX, Indexes.PEERCONNECTIONSSRCS_PEERCONNECTIONSSRCS_PEERCONNECTION_INDEX, Indexes.PEERCONNECTIONSSRCS_PEERCONNECTIONSSRCS_UPDATED_INDEX);
    }

    @Override
    public UniqueKey<PeerconnectionssrcsRecord> getPrimaryKey() {
        return Keys.KEY_PEERCONNECTIONSSRCS_PRIMARY;
    }

    @Override
    public List<UniqueKey<PeerconnectionssrcsRecord>> getKeys() {
        return Arrays.<UniqueKey<PeerconnectionssrcsRecord>>asList(Keys.KEY_PEERCONNECTIONSSRCS_PRIMARY);
    }

    @Override
    public Peerconnectionssrcs as(String alias) {
        return new Peerconnectionssrcs(DSL.name(alias), this);
    }

    @Override
    public Peerconnectionssrcs as(Name alias) {
        return new Peerconnectionssrcs(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Peerconnectionssrcs rename(String name) {
        return new Peerconnectionssrcs(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Peerconnectionssrcs rename(Name name) {
        return new Peerconnectionssrcs(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<byte[], Long, byte[], LocalDateTime, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
