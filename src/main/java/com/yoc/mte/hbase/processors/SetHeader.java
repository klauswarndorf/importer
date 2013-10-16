/**
 *
 */
package com.yoc.mte.hbase.processors;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author klaus warndorf<klaus.warndorf@yoc.com>
 *
 */
public class SetHeader implements Processor {

    private List<String> header;

    @Override
    public void process(Exchange exchange) throws Exception {
        @SuppressWarnings("unchecked")
        List<List<String>> data = (List<List<String>>) exchange.getIn().getBody();
        int i = 0;
        for (List<String> line : data) {
            String pf = "";
            if (i != 0) {
                pf = String.valueOf(i);
            }
            if(i != 0) {
                int n = 0;
                for(String value: line) {
                    exchange.getIn().setHeader("CamelHBaseRowId" + pf, this.getId(line));
                    exchange.getIn().setHeader("CamelHBaseFamily" + pf, "cf");
                    exchange.getIn().setHeader("CamelHBaseQualifier" + pf, this.getHeader(n));
                    exchange.getIn().setHeader("CamelHBaseValue" + pf, value);

                    n++;
                }
            }
            else {
                this.setHeader(line);
            }
            i++;
        }
    }

    private String getId(List<String> line) {
        return line.get(0) +"-"+ line.get(1) +"-"+ line.get(2);
    }

    private void setHeader(List<String> line) {
        this.header = line;
    }

    private String getHeader(int pos) {
        return this.header.get(pos);
    }
}
