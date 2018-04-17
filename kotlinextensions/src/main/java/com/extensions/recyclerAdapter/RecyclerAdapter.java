package com.extensions.recyclerAdapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.extensions.recyclerAdapter.ex.emptyerror.RecyclerEmptyLoader;
import com.extensions.recyclerAdapter.ex.emptyerror.RecyclerExEmptyViewHolder;
import com.extensions.recyclerAdapter.ex.loadmore.RecyclerExLoadMoreViewHolder;
import com.extensions.recyclerAdapter.ex.loadmore.RecyclerMoreLoader;
import com.extensions.recyclerAdapter.viewinjector.IViewInjector;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends AbstractRecyclerAdapter {
    public final static int TYPE_LOAD_MORE = -10;
    public final static int TYPE_EMPTY_VIEW = -20;

    private boolean isEmptyViewVisible;
    private boolean isLoadMoreReverse;

    private RecyclerMoreLoader moreLoader;
    private RecyclerEmptyLoader emptyViewLoader;

    private List itemList;
    private List<Type> dataTypes;
    private Map<Type, Object> creators;
    private Object defaultCreator = null;

    private RecyclerAdapter() {
        isEmptyViewVisible = false;
        isLoadMoreReverse = false;
        itemList = new ArrayList<>();
        dataTypes = new ArrayList<>();
        creators = new HashMap<>();
    }

    public static RecyclerAdapter create() {
        return new RecyclerAdapter();
    }

    public static <T extends RecyclerAdapter> T create(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getItem(int position) {
        if(emptyViewLoader != null && isEmptyViewVisible && position == 0 && itemList.size() <= 0)
            return emptyViewLoader;
        else if (moreLoader != null && ((isLoadMoreReverse && position == 0) || ((!isLoadMoreReverse) && position == itemList.size())))
            return moreLoader;
        else {
            if(isLoadMoreReverse && itemList.size() >= position)
                return itemList.get(position-1);
            else if(!isLoadMoreReverse && itemList.size() > position)
                return itemList.get(position);
            else
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (emptyViewLoader != null && isEmptyViewVisible && position == 0 && itemList.size() <= 0)
            return TYPE_EMPTY_VIEW;
        else if (moreLoader != null && ((isLoadMoreReverse && position == 0) || ((!isLoadMoreReverse) && position == itemList.size()))) {
            return TYPE_LOAD_MORE;
        } else {
            Object item = itemList.get(isLoadMoreReverse ? position-1 : position);
            int index = dataTypes.indexOf(item.getClass());
            if (index == -1)
                dataTypes.add(item.getClass());
            index = dataTypes.indexOf(item.getClass());
            return index;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size() <= 0 ? (emptyViewLoader != null && isEmptyViewVisible ? 1 : 0) : itemList.size() + (moreLoader == null ? 0 : 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY_VIEW) {
            return new RecyclerExEmptyViewHolder(emptyViewLoader.getLoadEmptyErrorView());
        } else if (viewType == TYPE_LOAD_MORE)
            return new RecyclerExLoadMoreViewHolder(moreLoader.getLoadMoreView());
        else {
            Type dataType = dataTypes.get(viewType);
            Object creatorValue = creators.get(dataType);
            if (creatorValue instanceof IViewHolderCreator) {
                IViewHolderCreator creator = (IViewHolderCreator) creatorValue;
                return creator.create(parent);
            } else if(creatorValue instanceof IBindingViewHolderCreator) {
                IBindingViewHolderCreator creator = (IBindingViewHolderCreator) creatorValue;
                return creator.create(parent);
            } else {
                if (creatorValue == null) {
                    for (Type t : creators.keySet()) {
                        if (isTypeMatch(t, dataType)) {
                            creatorValue = creators.get(t);
                            break;
                        }
                    }
                }
                if (creatorValue == null && defaultCreator != null)
                    creatorValue = defaultCreator;
                else
                    throw new IllegalArgumentException(String.format("Neither the TYPE: %s not The DEFAULT injector found...", dataType));
                return ((IBindingViewHolderCreator) creatorValue).create(parent);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isTypeMatch(Type type, Type targetType) {
        if (type instanceof Class && targetType instanceof Class) {
            if (((Class) type).isAssignableFrom((Class) targetType))
                return true;
        } else if (type instanceof ParameterizedType && targetType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            ParameterizedType parameterizedTargetType = (ParameterizedType) targetType;
            if (isTypeMatch(parameterizedType.getRawType(), ((ParameterizedType) targetType).getRawType())) {
                Type[] types = parameterizedType.getActualTypeArguments();
                Type[] targetTypes = parameterizedTargetType.getActualTypeArguments();
                if (types == null || targetTypes == null || types.length != targetTypes.length) {
                    return false;
                }
                int len = types.length;
                for (int i = 0; i < len; i++) {
                    if (!isTypeMatch(types[i], targetTypes[i])) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public RecyclerAdapter registerBindingDefault(final int layoutRes, final int brVariable, final RecyclerBindingInjector recyclerBindingInjector) {
        defaultCreator = new IBindingViewHolderCreator() {
            @Override
            public RecyclerTypeBindingViewHolder create(ViewGroup parent) {
                return new RecyclerTypeBindingViewHolder(parent, layoutRes, brVariable) {
                    @Override
                    protected void onBind(int position, Object item, ViewDataBinding binding) {
                        recyclerBindingInjector.onInject(position, item, binding);
                    }
                };
            }
        };
        return this;
    }

    public <T, V extends ViewDataBinding> RecyclerAdapter registerBinding(final int layoutRes, final int brVariable, final RecyclerBindingInjector<T, V> recyclerBindingInjector) {
        Type type = getRecyclerBindingInjectorActualTypeArguments(recyclerBindingInjector);
        if (type == null) {
            throw new IllegalArgumentException();
        }
        creators.put(type, new IBindingViewHolderCreator<T, V>() {
            @Override
            public RecyclerTypeBindingViewHolder<T, V> create(ViewGroup parent) {
                return new RecyclerTypeBindingViewHolder<T, V>(parent, layoutRes, brVariable) {
                    @Override
                    protected void onBind(int position, T item, V binding) {
                        recyclerBindingInjector.onInject(position, item, binding);
                    }
                };
            }
        });
        return this;
    }

    public RecyclerAdapter registerDefault(final int layoutRes, final RecyclerInjector recyclerInjector) {
        defaultCreator = new IViewHolderCreator() {
            @Override
            public RecyclerTypeViewHolder create(ViewGroup parent) {
                return new RecyclerTypeViewHolder(parent, layoutRes) {
                    @Override
                    protected void onBind(int position, Object item, IViewInjector injector) {
                        recyclerInjector.onInject(position, item, injector);
                    }
                };
            }
        };
        return this;
    }

    public <T> RecyclerAdapter register(final int layoutRes, final RecyclerInjector<T> recyclerInjector) {
        Type type = getRecyclerInjectorActualTypeArguments(recyclerInjector);
        if (type == null) {
            throw new IllegalArgumentException();
        }
        creators.put(type, new IViewHolderCreator<T>() {
            @Override
            public RecyclerTypeViewHolder<T> create(ViewGroup parent) {
                return new RecyclerTypeViewHolder<T>(parent, layoutRes) {
                    @Override
                    protected void onBind(int position, T item, IViewInjector injector) {
                        recyclerInjector.onInject(position, item, injector);
                    }
                };
            }
        });
        return this;
    }

    private <T, V extends ViewDataBinding> Type getRecyclerBindingInjectorActualTypeArguments(RecyclerBindingInjector<T, V> recyclerBindingInjector) {
        Type[] interfaces = recyclerBindingInjector.getClass().getGenericInterfaces();
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType) {
                if (((ParameterizedType) type).getRawType().equals(RecyclerBindingInjector.class)) {
                    Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
                    if (actualType instanceof Class) {
                        return actualType;
                    } else {
                        throw new IllegalArgumentException("The generic type argument of RecyclerBindingInjector is NOT support Generic Parameterized Type now, Please using a WRAPPER class install of it directly.");
                    }
                }
            }
        }
        return null;
    }

    private <T> Type getRecyclerInjectorActualTypeArguments(RecyclerInjector<T> recyclerInjector) {
        Type[] interfaces = recyclerInjector.getClass().getGenericInterfaces();
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType) {
                if (((ParameterizedType) type).getRawType().equals(RecyclerInjector.class)) {
                    Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
                    if (actualType instanceof Class) {
                        return actualType;
                    } else {
                        throw new IllegalArgumentException("The generic type argument of RecyclerBindingInjector is NOT support Generic Parameterized Type now, Please using a WRAPPER class install of it directly.");
                    }
                }
            }
        }
        return null;
    }

    public RecyclerAdapter attachTo(RecyclerView... recyclerViews) {
        for (RecyclerView recyclerView : recyclerViews) {
            recyclerView.setAdapter(this);
        }
        return this;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (moreLoader != null)
            recyclerView.addOnScrollListener(moreLoader);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (moreLoader != null)
            recyclerView.removeOnScrollListener(moreLoader);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public RecyclerAdapter enableLoadMore(boolean isLoadMoreReverse, RecyclerMoreLoader recyclerMoreLoader) {
        this.isLoadMoreReverse = isLoadMoreReverse;
        this.moreLoader = recyclerMoreLoader;
        recyclerMoreLoader.setRecyclerAdapter(this);
        notifyDataSetChanged();
        return this;
    }

    public RecyclerAdapter enableEmptyView(RecyclerEmptyLoader recyclerEmptyLoader) {
        this.emptyViewLoader = recyclerEmptyLoader;
        notifyDataSetChanged();
        return this;
    }

    private interface IBindingViewHolderCreator<T, V extends ViewDataBinding> {
        RecyclerTypeBindingViewHolder<T, V> create(ViewGroup parent);
    }

    private interface IViewHolderCreator<T> {
        RecyclerTypeViewHolder<T> create(ViewGroup parent);
    }

    private static abstract class RecyclerTypeBindingViewHolder<T, V extends ViewDataBinding> extends RecyclerBindingViewHolder<T, V> {
        RecyclerTypeBindingViewHolder(ViewGroup parent, int itemLayoutRes, int brVariable) {
            super(parent, itemLayoutRes, brVariable);
        }
    }

    private static abstract class RecyclerTypeViewHolder<T> extends RecyclerViewHolder<T> {
        RecyclerTypeViewHolder(ViewGroup parent, int itemLayoutRes) {
            super(parent, itemLayoutRes);
        }
    }

    public List<?> getItemList() {
        return itemList;
    }

    public void clear() {
        itemList.clear();
        isEmptyViewVisible = false;
        notifyDataSetChanged();
    }

    public void add(Object item, int... position) {
        if(position.length <= 0) {
            if (isLoadMoreReverse) {
                itemList.add(0, item);
                isEmptyViewVisible = itemList.size() <= 0;
                notifyItemInserted(0);
            } else {
                itemList.add(item);
                isEmptyViewVisible = itemList.size() <= 0;
                notifyItemInserted(itemList.size() - 1);
            }
        } else {
            itemList.add(position[0], item);
            isEmptyViewVisible = itemList.size() <= 0;
            notifyItemInserted(position[0]);
        }
        reset();
    }

    public void addAll(List list, int... position) {
        if(position.length <= 0) {
            if (isLoadMoreReverse) {
                itemList.addAll(0, list);
                isEmptyViewVisible = itemList.size() <= 0;
                notifyItemRangeInserted(0, list.size());
            } else {
                itemList.addAll(list);
                isEmptyViewVisible = itemList.size() <= 0;
                notifyItemRangeInserted(itemList.size() - 1, list.size());
            }
        } else {
            itemList.addAll(position[0], list);
            isEmptyViewVisible = itemList.size() <= 0;
            notifyItemRangeInserted(position[0], list.size());
        }
        reset();
    }

    public void addAllWithClear(List list, int... position) {
        clear();
        addAll(list, position);
        reset();
    }

    public void remove(Object item) {
        int index = itemList.indexOf(item);
        if (index != -1) {
            itemList.remove(index);
            isEmptyViewVisible = itemList.size() <= 0;
            notifyItemRemoved(index);
        }
    }

    private void reset() {
        if(moreLoader != null && moreLoader.isLoading()) {
            moreLoader.reset();
        }
    }

    private void error() {
        if(moreLoader != null)
            moreLoader.error();
    }
}
