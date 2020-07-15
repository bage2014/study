package com.bage;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

public class ApplicationReadFolder {

    public static void main(String[] args) throws Exception {
        new ApplicationReadFolder().run();
    }

    public void run(String... strings) throws Exception {
        ExchangeService exchangeService = buildSerivce();
        Folder folder = filterFolder(exchangeService, "balabala");
        if (Objects.nonNull(folder)) {
            printFolderBody(exchangeService, folder, 100);
        }
    }

    private ExchangeService buildSerivce() throws Exception {
        ExchangeService service = new ExchangeService();
        ExchangeCredentials credentials = new WebCredentials("rh_lu@trip.com", "xxxx");
        service.setCredentials(credentials);
        service.setUrl(new URI("https://webmail.ctrip.com/EWS/Exchange.asmx"));
        return service;
    }

    private Folder filterFolder(ExchangeService service, String displayName) throws Exception {
        FindFoldersResults findResults = service.findFolders(WellKnownFolderName.MsgFolderRoot, new FolderView(Integer.MAX_VALUE));
        for (Folder folder : findResults.getFolders()) {
            if (Objects.equals(folder.getDisplayName(), displayName)) {
                return folder;
            }
        }
        return null;
    }

    public void printFolderBody(ExchangeService service, Folder folder, int maxSize) throws Exception {
        ItemView view = new ItemView(maxSize);
        FindItemsResults<Item> findResults = service.findItems(folder.getId(), view);
        ArrayList<Item> items = findResults.getItems();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.load();
            System.out.println("i = " + i + " ; body = " + item.getBody());
        }
    }
}


