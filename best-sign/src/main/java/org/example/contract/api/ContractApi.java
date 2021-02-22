package org.example.contract.api;

import org.example.contract.pojo.CreateSignature;
import org.example.contract.pojo.Register;
import org.example.contract.pojo.QueryRegister;

public interface ContractApi {

    /**
     * Apply personal or enterprise signature.
     *
     * @param pr p
     * @return r  taskId
     */
    String register(Register pr);

    /**
     * Apply enterprise signature.
     *
     * @param pr p
     * @return r  taskId
     */
    String enterpriseRegister(Register pr);

    /**
     * Query signature status.
     *
     * @see QueryRegister.RegisterEnum
     * @param qr p
     * @return s
     */
    boolean queryRegister(QueryRegister qr);

    /**
     * Create personal signature.
     *
     * @param cs p
     */
    void createPersonalSignature(CreateSignature cs);

    /**
     * Create enterprise signature.
     *
     * @param cs p
     */
    void createEnterpriseSignature(CreateSignature cs);
}
