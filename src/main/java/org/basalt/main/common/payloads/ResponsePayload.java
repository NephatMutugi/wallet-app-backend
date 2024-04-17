package org.basalt.main.common.payloads;

import lombok.Data;

import java.util.Objects;

/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
@Data
public class ResponsePayload<T> extends ApiResponse {
    private T data;

    public ResponsePayload(Header header, T data){
        super(header);
        this.data = data;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ResponsePayload<?> that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), data);
    }
}
